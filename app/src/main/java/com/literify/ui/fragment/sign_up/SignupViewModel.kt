package com.literify.ui.fragment.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.literify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> get() = _signupState

    fun signup(firstName: String, lastName: String, email: String, password: String) {
        _signupState.value = SignupState.Loading

        viewModelScope.launch {
            try {
                val user = authRepository.registerWithEmailPassword(firstName,lastName,email, password)
                _signupState.value = SignupState.Success(user)
            } catch (e: Exception) {
                _signupState.value = SignupState.Error(e.message ?: "Signup failed")
            }
        }
    }
}

sealed class SignupState {
    object Loading : SignupState()
    data class Success(val user: FirebaseUser?) : SignupState()
    data class Error(val message: String) : SignupState()
}