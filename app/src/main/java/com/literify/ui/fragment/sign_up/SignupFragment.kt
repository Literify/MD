package com.literify.ui.fragment.sign_up

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
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

        initializeUI()
        setupObservers()
    }

    private fun initializeUI() {
        val firstNameInput = binding.inputFirstName.editText
        val lastNameInput = binding.inputLastName.editText
        val emailInput = binding.inputEmail.editText
        val passwordInput = binding.inputPassword.editText
        val confirmPasswordInput = binding.inputConfirmPassword.editText

        binding.apply {
            firstNameInput?.apply {
                addTextChangedListener {
                    inputFirstName.isErrorEnabled = false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus && text.isEmpty()) {
                        inputFirstName.error =
                            "${getString(R.string.first_name)} ${getString(R.string.validation_error_required)}"
                    }
                }
            }

            lastNameInput?.apply {
                addTextChangedListener {
                    inputLastName.isErrorEnabled = false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus && text.isEmpty()) {
                        inputLastName.error =
                            "${getString(R.string.last_name)} ${getString(R.string.validation_error_required)}"
                    }
                }
            }

            emailInput?.apply {
                addTextChangedListener {
                    inputEmail.isErrorEnabled = false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        if (!isEmailValid(text.toString())) {
                            inputEmail.error = getString(R.string.validation_error_email)
                        }

                        if (text.isEmpty()) {
                            inputEmail.error =
                                "${getString(R.string.email)} ${getString(R.string.validation_error_required)}"
                        }
                    }
                }
            }

            passwordInput?.apply {
                addTextChangedListener {
                    inputPassword.isErrorEnabled = false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        if (!isPasswordValid(text.toString())) {
                            inputPassword.error = getString(R.string.validation_error_password)
                        }

                        if (text.isEmpty()) {
                            inputPassword.error =
                                "${getString(R.string.password)} ${getString(R.string.validation_error_required)}"
                        }
                    }
                }
            }

            confirmPasswordInput?.apply {
                addTextChangedListener {
                    inputConfirmPassword.isErrorEnabled = false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        if (passwordInput?.text.toString() != text.toString()) {
                            inputConfirmPassword.error = getString(R.string.validation_error_confirm_password)
                        }

                        if (text.isEmpty()) {
                            inputConfirmPassword.error =
                                "${getString(R.string.confirm_password)} ${getString(R.string.validation_error_required)}"
                        }
                    }
                }
            }

            buttonSignup.setOnClickListener {
                val firstName = firstNameInput?.text.toString()
                val lastName = lastNameInput?.text.toString()
                val email = emailInput?.text.toString()
                val password = passwordInput?.text.toString()

                firstNameInput?.requestFocus()
                lastNameInput?.requestFocus()
                emailInput?.requestFocus()
                passwordInput?.requestFocus()
                confirmPasswordInput?.requestFocus()
                confirmPasswordInput?.clearFocus()

                val isNoError = !inputFirstName.isErrorEnabled && !inputLastName.isErrorEnabled &&
                        !inputEmail.isErrorEnabled && !inputPassword.isErrorEnabled && !inputConfirmPassword.isErrorEnabled

                if (isNoError) {
                    viewModel.signup(firstName, lastName, email, password)
                } else {
                    showError(getString(R.string.validation_error_submit))
                }
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

    // TODO: Show error message according to ui/ux plan
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}