package com.sanxynet.hagglex.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.sanxynet.hagglex.R
import com.sanxynet.hagglex.ResendVerificationCodeQuery
import com.sanxynet.hagglex.VerifyUserMutation
import com.sanxynet.hagglex.utils.apolloClient
import com.sanxynet.type.EmailInput
import com.sanxynet.type.VerifyUserInput
import com.google.android.material.snackbar.Snackbar
import com.sanxynet.hagglex.databinding.FragmentVerificationBinding

class VerificationFragment : Fragment() {
    private var _binding: FragmentVerificationBinding? = null
    private val binding
        get() = _binding!!

    private val args: VerificationFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.verificationPageBtn.setOnClickListener {

            val verifyUserInput =
                VerifyUserInput(binding.verificationPageCodeEt.text.toString().toInt())

            apolloClient(args.token).mutate(VerifyUserMutation(Input.optional(verifyUserInput)))
                .enqueue(object : ApolloCall.Callback<VerifyUserMutation.Data>() {
                    override fun onResponse(response: Response<VerifyUserMutation.Data>) {
                        response.data?.verifyUser?.token?.let {
                            activity?.runOnUiThread {
                                Snackbar.make(view, "Account Verified", Snackbar.LENGTH_LONG).show()
                                findNavController().navigate(R.id.setUpComplete)
                                Log.d("Verification Page", "Bearer ${args.token}")
                            }
                        }

                        response.errors?.get(0)?.message?.let {
                                activity?.runOnUiThread {
                                    Snackbar.make(
                                        view,
                                        "${response.errors?.get(0)?.message}",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                    Log.d("Verification Page", "Bearer ${args.token}")

                            }
                        }
                    }

                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                        Log.d("VERIFICATION", e.localizedMessage)
                    }

                })

        }

        binding.verificationPageResendBtn.setOnClickListener {
            val email = EmailInput(args.email)

            apolloClient().query(ResendVerificationCodeQuery(Input.fromNullable(email)))
                .enqueue(
                    object : ApolloCall.Callback<ResendVerificationCodeQuery.Data>() {
                        override fun onResponse(response: Response<ResendVerificationCodeQuery.Data>) {

                            var result = response.data?.resendVerificationCode

                            result?.let {

                                if (it) {
                                    activity?.runOnUiThread {
                                        Snackbar.make(
                                            view,
                                            "Verification Link has been send",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    activity?.runOnUiThread {
                                        Snackbar.make(view, "Try Again", Snackbar.LENGTH_LONG)
                                            .show()
                                    }
                                }

                            }


                        }

                        override fun onFailure(e: ApolloException) {
                            e.printStackTrace()
                        }

                    }
                )
        }

        binding.verificationBackBtn.setOnClickListener {
            backButtonNavigation()
        }

    }


    fun backButtonNavigation(){
        findNavController().navigate(R.id.register)
    }

}