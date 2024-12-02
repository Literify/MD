package com.literify.ui.fragment.sign_in

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.literify.R
import com.literify.databinding.FragmentSigninBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigninFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SigninViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            buttonSignin.setOnClickListener {
                val identifier = binding.inputId.editText?.text.toString()
                val password = binding.inputPassword.editText?.text.toString()

                if (identifier.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(identifier, password)
                }
            }

            buttonSigninGoogle.setOnClickListener {
                // TODO: Implement Google Sign-In
            }

            buttonForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }

            buttonSignup.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Loading -> {
                    binding.buttonSignin.apply {
                        isEnabled = false
                        text = ""
                    }
                    binding.progressSignin.visibility = View.VISIBLE
                }
                is LoginState.Success -> {
                    binding.buttonSignin.apply {
                        isEnabled = true
                        text = getString(R.string.sign_in)
                    }
                    binding.progressSignin.visibility = View.GONE

                    findNavController().navigate(R.id.mainActivity)
                }
                is LoginState.Error -> {
                    binding.buttonSignin.apply {
                        isEnabled = true
                        text = getString(R.string.sign_in)
                    }
                    binding.progressSignin.visibility = View.GONE

                    // TODO: Show error message
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}