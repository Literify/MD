package com.literify.ui.fragment.sign_up

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.literify.R
import com.literify.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonSignup.setOnClickListener {
                val firstName = binding.inputFirstName.editText?.text.toString()
                val lastName = binding.inputLastName.editText?.text.toString()
                val email = binding.inputEmail.editText?.text.toString()
                val password = binding.inputPassword.editText?.text.toString()

                //  TODO : Implement confirmPassword
                val confirmPassword = null

                // TODO: Implement email & password validation
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signup(firstName, lastName, email, password)
                } else {
                    binding.apply {
                        inputFirstName.error = if (firstName.isEmpty()) getString(R.string.required) else null
                        inputLastName.error = if (lastName.isEmpty()) getString(R.string.required) else null
                        inputEmail.error = if (email.isEmpty()) getString(R.string.required) else null
                        inputPassword.error = if (password.isEmpty()) getString(R.string.required) else null
                    }
                }
            }

            buttonSignupGoogle.setOnClickListener {
                // TODO : Implement Google Sign-Up
            }
        }

        viewModel.signupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignupState.Loading -> {
                    binding.buttonSignup.apply {
                        isEnabled = false
                        text = ""
                    }
                    binding.progressSignup.visibility = View.VISIBLE
                }
                is SignupState.Success -> {
                    binding.buttonSignup.apply {
                        isEnabled = true
                        text = getString(R.string.sign_up)
                    }
                    binding.progressSignup.visibility = View.GONE

                    findNavController().navigate(R.id.mainActivity)
                }
                is SignupState.Error -> {
                    binding.buttonSignup.apply {
                        isEnabled = true
                        text = getString(R.string.sign_up)
                    }
                    binding.progressSignup.visibility = View.GONE

                    // TODO: Show error message
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}