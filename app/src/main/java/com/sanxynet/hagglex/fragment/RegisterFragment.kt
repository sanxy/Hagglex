package com.sanxynet.hagglex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.sanxynet.hagglex.*
import com.sanxynet.hagglex.databinding.FragmentRegisterBinding
import com.sanxynet.hagglex.utils.apolloClient
import com.sanxynet.hagglex.utils.showErrorMessage
import com.sanxynet.hagglex.utils.showSnackbar
import com.sanxynet.hagglex.utils.validateRegisteredField
import com.sanxynet.hagglex.viewmodel.MainViewModel
import com.sanxynet.type.PhoneNumberDetailsInput


class RegisterFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private  var _binding : FragmentRegisterBinding? = null
    private val binding
     get() = _binding!!

    companion object {
        var userToken: String = ""
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountBackBtn.setOnClickListener {
//            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.logIn)
        }

        binding.createAccountSignUpBtn.setOnClickListener {

            if(validateRegisteredField(binding.createAccountEmailEt.text.toString().trim(),binding.createAccountPasswordEt.text.toString().trim(),
                    binding.createAccountUsernameEt.text.toString().trim(),binding.createAccountPhoneNumber.text.toString().trim()

                ) == "success"){
                createUser(view, binding.createAccountEmailEt.text.toString().trim(),
                    binding.createAccountPasswordEt.text.toString().trim(),
                    binding.createAccountPhoneNumber.text.toString().trim()
                ,binding.createAccountUsernameEt.text.toString().trim())
            }else {

                showErrorMessage(view, validateRegisteredField(binding.createAccountEmailEt.text.toString(),binding.createAccountPasswordEt.text.toString(),
                    binding.createAccountUsernameEt.text.toString(),binding.createAccountPhoneNumber.text.toString()

                )
                )

            }

        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun createUser(view:View, email : String, password : String, number: String, username: String){
        binding.createAccountProgressBar.isVisible = true
        val phoneNumber = PhoneNumberDetailsInput(number,binding.createAccountCcp.defaultCountryName,binding.createAccountCcp.defaultCountryCode)
        val input : Input<PhoneNumberDetailsInput> = Input.fromNullable(phoneNumber)
        val createUserInput = com.sanxynet.type.CreateUserInput(email, username,password,
            number, Input.fromNullable(null),input,binding.createAccountCcp.defaultCountryName,binding.createAccountCcp.defaultCountryName)
        apolloClient().mutate(
            RegisterMutation(data = Input.optional(createUserInput)
            )
        ).enqueue(object : ApolloCall.Callback<RegisterMutation.Data>(){
            override fun onResponse(response: Response<RegisterMutation.Data>) {

                response.data?.register?.let {
                    activity?.runOnUiThread {
                        val action = RegisterFragmentDirections.actionCreateAccountPageToVerificationPage(
                            response.data!!.register!!.user.email, response.data!!.register!!.token)
                        viewModel.updateUserName(response.data!!.register!!.user.username)
                        userToken = response.data!!.register!!.token
                        findNavController().navigate(action)
                        showSnackbar(view,"Account has been Created")

                        binding.createAccountProgressBar.isVisible = false
                    }
                }


                response.errors?.let {
                    activity?.runOnUiThread {
                        binding.createAccountProgressBar.isVisible = false
                        showSnackbar(view,response.errors!![0].message )
                    }
                }

            }

            override fun onFailure(e: ApolloException) {
                activity?.runOnUiThread {
                    binding.createAccountProgressBar.isVisible = false
                }
                e.printStackTrace()
            }

        })

    }


}