package com.literify.ui.fragment.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.literify.R
import com.literify.data.repository.AuthRepository
import com.literify.util.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val string: StringProvider
) : ViewModel() {

    private val _signinState = MutableLiveData<SigninState>()
    val signinState: LiveData<SigninState> get() = _signinState

    fun loginWithPassword(email: String, password: String) {
        _signinState.value = SigninState.Loading("1")

        viewModelScope.launch {
            try {
                val user = authRepository.signinWithEmailPassword(email, password)
                _signinState.value = SigninState.Success(user)
            } catch (e: Exception) {
                _signinState.value = SigninState.Error(e.message ?: string.getString(R.string.error_default))
            }
        }
    }

    fun loginWithGoogleIdToken(idToken: String?) {
        _signinState.value = SigninState.Loading("2")

        viewModelScope.launch {
            try {
                val user = authRepository.signInWithGoogleIdToken(idToken)
                _signinState.value = SigninState.Success(user)
            } catch (e: Exception) {
                _signinState.value = SigninState.Error(e.message ?: string.getString(R.string.error_default))
            }
        }
    }
}

sealed class SigninState {
    data class Loading(val button: String) : SigninState()
    data class Success(val user: FirebaseUser?) : SigninState()
    data class Error(val message: String) : SigninState()
}