package com.literify.ui.fragment.sign_up

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.literify.R
import com.literify.databinding.FragmentSignupBinding
import com.literify.ui.activity.main.MainActivity
import com.literify.ui.activity.main.MainActivity.Companion.EXTRA_SAVE_CREDENTIAL
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

        handleButtonClickListener()
        setupObservers()
    }

    private fun handleButtonClickListener() {
        binding.buttonSignup.setOnClickListener {
            val firstName = binding.inputFirstName.editText?.text.toString()
            val lastName = binding.inputLastName.editText?.text.toString()
            val email = binding.inputEmail.editText?.text.toString()
            val password = binding.inputPassword.editText?.text.toString()
            val confirmPassword = binding.inputConfirmPassword.editText?.text.toString()

            binding.apply {
                inputFirstName.isErrorEnabled = false
                inputLastName.isErrorEnabled = false
                inputEmail.isErrorEnabled = false
                inputPassword.isErrorEnabled = false
                inputConfirmPassword.isErrorEnabled = false
            }

            if (!isEmailValid(email)) {
                binding.inputEmail.error = getString(R.string.validation_error_email)
            }

            if (!isPasswordValid(password)) {
                binding.inputPassword.error = getString(R.string.validation_error_password)
            }

            if (password != confirmPassword) {
                binding.inputConfirmPassword.error =
                    getString(R.string.validation_error_confirm_password)
            }

            val inputs = listOf(
                binding.inputFirstName to firstName,
                binding.inputLastName to lastName,
                binding.inputEmail to email,
                binding.inputPassword to password,
                binding.inputConfirmPassword to confirmPassword
            )

            var isValid = true
            inputs.forEach { (input, value) ->
                if (value.isEmpty()) {
                    input.error =
                        "${input.hint.toString()} ${getString(R.string.validation_error_required)}"
                    isValid = false
                } else {
                    input.error = null
                }
            }

            if (isValid) {
                viewModel.signup(firstName, lastName, email, password)
            }
        }
    }

    private fun setupObservers() {
        viewModel.signupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignupState.Loading -> {
                    showLoading(true)
                }
                is SignupState.Success -> {
                    showLoading(false)

                    if(state.user != null) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        val isEmailLogin = state.user.providerData.any { it.providerId == "password" }
                        if (isEmailLogin) {
                            intent.putExtra(EXTRA_SAVE_CREDENTIAL, true)
                        }

                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                is SignupState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6 && password.matches(".*[a-z].*".toRegex()) &&
                password.matches(".*[A-Z].*".toRegex()) && password.matches(".*[0-9].*".toRegex())
    }

    private fun showLoading(show: Boolean) {
        binding.buttonSignup.apply {
            isEnabled = !show
            text = if (!show) getString(R.string.sign_up) else ""
        }
        binding.progressSignup.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        // TODO: Show error message according to ui/ux plan
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}