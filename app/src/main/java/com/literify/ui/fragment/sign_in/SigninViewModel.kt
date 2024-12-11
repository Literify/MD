package com.literify.ui.fragment.sign_in

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
class SigninViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signinState = MutableLiveData<SigninState>()
    val signinState: LiveData<SigninState> get() = _signinState

    fun loginWithPassword(email: String, password: String) {
        _signinState.value = SigninState.Loading("1")

        viewModelScope.launch {
            try {
                val user = authRepository.loginWithEmailPassword(email, password)
                _signinState.value = SigninState.Success(user)
            } catch (e: Exception) {
                _signinState.value = SigninState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun loginWithGoogleIdToken(idToken: String?) {
        _signinState.value = SigninState.Loading("2")

        viewModelScope.launch {
            try {
                val user = authRepository.loginWithGoogleIdToken(idToken)
                _signinState.value = SigninState.Success(user)
            } catch (e: Exception) {
                _signinState.value = SigninState.Error(e.message ?: "Login with Google failed")
            }
        }
    }
}

sealed class SigninState {
    data class Loading(val button: String) : SigninState()
    data class Success(val user: FirebaseUser?) : SigninState()
    data class Error(val message: String) : SigninState()
}