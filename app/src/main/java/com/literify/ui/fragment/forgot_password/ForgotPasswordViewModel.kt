package com.literify.ui.fragment.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.literify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _resetPasswordState = MutableLiveData<ResetPasswordState>()
    val resetPasswordState: LiveData<ResetPasswordState> get() = _resetPasswordState

    fun requestPasswordReset(identifier: String) {
        _resetPasswordState.value = ResetPasswordState.Loading

        viewModelScope.launch {
            try {
                authRepository.sendPasswordResetEmail(identifier)
                _resetPasswordState.value = ResetPasswordState.Success("Password reset email sent")
            } catch (e: Exception) {
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Failed to send password reset email")
            }
        }
    }

    fun confirmPasswordReset(oobCode: String, newPassword: String) {
        _resetPasswordState.value = ResetPasswordState.Loading

        viewModelScope.launch {
            try {
                authRepository.confirmPasswordReset(oobCode, newPassword)
                _resetPasswordState.value = ResetPasswordState.Success("Password reset successful")
            } catch (e: Exception) {
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Failed to reset password")
            }
        }
    }
}

sealed class ResetPasswordState {
    object Loading : ResetPasswordState()
    data class Success(val message: String) : ResetPasswordState()
    data class Error(val errorMessage: String) : ResetPasswordState()
}