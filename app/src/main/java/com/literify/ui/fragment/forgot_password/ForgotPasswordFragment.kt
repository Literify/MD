package com.literify.ui.fragment.forgot_password

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.literify.R
import com.literify.databinding.FragmentForgotPasswordBinding
import com.literify.util.InputValidator.isPasswordValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        val deeplink = arguments?.getString("oobCode")
        deeplink?.let {
            try {
                showResetPassword(deeplink)
            } catch (e: Exception) {
                Log.e("ForgotPasswordFragment", "Error: ${e.message}")
            }

            arguments?.putString("oobCode", null)
            return
        }

        showFindAccount()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO: Implement the following functions
    private fun setupObservers() {
        viewModel.resetPasswordState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResetPasswordState.Loading -> {
                    showLoading(true)
                }
                is ResetPasswordState.Success -> {
                    showLoading(false)

                    showError(state.message)
                    findNavController().navigateUp()
                }
                is ResetPasswordState.Error -> {
                    showLoading(false)

                    showError(state.errorMessage)
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun showFindAccount() {
        binding.apply {
            tvTitle.text = getString(R.string.title_find_account)
            tvSubtitle.text = getString(R.string.body_find_account_msg)

            input1.apply {
                hint = getString(R.string.body_identifier)
                editText?.apply {
                    addTextChangedListener {
                        input1.isErrorEnabled = false
                    }
                    setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus && text.isEmpty()) {
                            input1.error =
                                "${getString(R.string.body_identifier)} ${getString(R.string.error_validation_required)}"
                        }
                    }
                }
            }
            input1Edit.inputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE

            buttonAction.setOnClickListener {
                val id = binding.input1.editText?.text.toString()

                input1.editText?.requestFocus()
                input1.editText?.clearFocus()

                val isNoError = !input1.isErrorEnabled
                if (isNoError) {
                    viewModel.requestPasswordReset(id)
                } else {
                    showError(getString(R.string.error_validation_submit))
                }
            }
        }
    }

    private fun showResetPassword(oobCode: String) {
        binding.apply {
            tvTitle.text = getString(R.string.title_create_password)
            tvSubtitle.text = getString(R.string.body_create_password_msg)

            input1.apply {
                hint = getString(R.string.password)
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                editText?.apply {
                    addTextChangedListener {
                        input1.isErrorEnabled = false
                    }
                    setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            if (!isPasswordValid(text.toString())) {
                                input1.error = getString(R.string.error_validation_password)
                            }
                            if (text.isEmpty()) {
                                input1.error =
                                    "${getString(R.string.password)} ${getString(R.string.error_validation_required)}"
                            }
                        }
                    }
                }
            }
            input1Edit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            input2.apply {
                visibility = View.VISIBLE
                hint = getString(R.string.confirm_password)
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE

                editText?.apply {
                    addTextChangedListener {
                        input2.isErrorEnabled = false
                    }
                    setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            if (input1.editText?.text.toString() != text.toString()) {
                                input2.error = getString(R.string.error_validation_confirm_password)
                            }
                            if (text.isEmpty()) {
                                input2.error =
                                    "${getString(R.string.confirm_password)} ${getString(R.string.error_validation_required)}"
                            }
                        }
                    }
                }
            }
            input2Edit.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            buttonAction.setOnClickListener {
                val newPassword = binding.input1.editText?.text.toString()

                input1.editText?.requestFocus()
                input2.editText?.requestFocus()
                input2.editText?.clearFocus()

                val isNoError = !input1.isErrorEnabled && !input2.isErrorEnabled
                if (isNoError) {
                    viewModel.confirmPasswordReset(oobCode, newPassword)
                } else {
                    showError(getString(R.string.error_validation_submit))
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            input1.isEnabled = !show
            if (input2.visibility == View.VISIBLE) input2.isEnabled = !show

            buttonAction.apply {
                isEnabled = !show
                text = if (!show) getString(R.string.next) else ""
            }
            progressAction.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    // TODO: Show error message according to ui/ux plan
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}