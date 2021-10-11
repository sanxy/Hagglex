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
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.sanxynet.hagglex.LoginUserMutation
import com.sanxynet.hagglex.R
import com.sanxynet.hagglex.utils.apolloClient
import com.sanxynet.hagglex.databinding.FragmentLogInBinding

import com.sanxynet.hagglex.utils.showSnackbar
import com.sanxynet.hagglex.viewmodel.MainViewModel
import com.sanxynet.type.LoginInput

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.welcomePageCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.register)
        }

        binding.welcomePageLogin.setOnClickListener {

            loginUser(
                binding.welcomePageEmailEt.text.toString().trim(),
                binding.welcomePagePasswordEt.text.toString().trim(),
                view
            )
        }

    }


    fun loginUser(email: String, password: String, view: View) {

        if (email.isEmpty() || password.isEmpty()) {

            if (email.isEmpty()) {
                showSnackbar(view, "Email field is Empty")
            } else {
                showSnackbar(view, "Password field is Empty")
            }

        } else {
            binding.welcomePageProgressBar.isVisible = true

            val login = LoginInput(email, password)

            apolloClient().mutate(LoginUserMutation(login)).enqueue(
                object : ApolloCall.Callback<LoginUserMutation.Data>() {
                    override fun onResponse(response: Response<LoginUserMutation.Data>) {
                        val res = response.data

                        res?.let {
                            if (it.login.token != null) {
                                activity?.runOnUiThread {
                                    findNavController().navigate(R.id.dashBoard)
                                    viewModel.updateLoginToken(res.login.token!!)
                                    viewModel.updateId(res.login.user._id)
                                    viewModel.updateUserName(res.login.user.username)
                                    binding.welcomePageProgressBar.isVisible = false

                                }
                            }
                        }

                        if (response.errors != null) {
                            activity?.runOnUiThread {
                                binding.welcomePageProgressBar.isVisible = false
                                showSnackbar(view, response.errors!![0].message)

                            }

                        }


                    }

                    override fun onFailure(e: ApolloException) {

                        activity?.runOnUiThread {
                            binding.welcomePageProgressBar.isVisible = false
                        }
                        e.printStackTrace()
                    }

                }
            )

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}