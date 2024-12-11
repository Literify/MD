package com.literify.ui.fragment.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.literify.R
import com.literify.data.repository.AuthRepository
import com.literify.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val string: StringProvider
): ViewModel() {

    private val _resetPasswordState = MutableLiveData<ResetPasswordState>()
    val resetPasswordState: LiveData<ResetPasswordState> get() = _resetPasswordState

    fun requestPasswordReset(identifier: String) {
        _resetPasswordState.value = ResetPasswordState.Loading

        viewModelScope.launch {
            try {
                authRepository.sendPasswordResetEmail(identifier)
                _resetPasswordState.value = ResetPasswordState.Success(string.getString(R.string.body_reset_password_msg))
            } catch (e: Exception) {
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: string.getString(R.string.error_default))
            }
        }
    }

    fun confirmPasswordReset(oobCode: String, newPassword: String) {
        _resetPasswordState.value = ResetPasswordState.Loading

        viewModelScope.launch {
            try {
                authRepository.confirmPasswordReset(oobCode, newPassword)
                _resetPasswordState.value = ResetPasswordState.Success(string.getString(R.string.success_reset_password))
            } catch (e: Exception) {
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: string.getString(R.string.error_default))
            }
        }
    }
}

sealed class ResetPasswordState {
    object Loading : ResetPasswordState()
    data class Success(val message: String) : ResetPasswordState()
    data class Error(val errorMessage: String) : ResetPasswordState()
}