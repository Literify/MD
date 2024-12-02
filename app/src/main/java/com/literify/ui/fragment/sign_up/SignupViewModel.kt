package com.literify.ui.fragment.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> get() = _signupState

    fun signup(firstName: String, lastName: String, email: String, password: String) {
        _signupState.value = SignupState.Loading

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.sendEmailVerification()

                    // TODO: Save user details to Firestore
                    // TODO: Implement email verification popup

                    _signupState.value = SignupState.Success(user)
                } else {
                    _signupState.value = SignupState.Error(task.exception?.message ?: "Signup failed")
                }
            }
    }

    fun signupWithGoogle() {
        // Implement Google Sign-Up
    }
}

sealed class SignupState {
    object Loading : SignupState()
    data class Success(val user: FirebaseUser?) : SignupState()
    data class Error(val message: String) : SignupState()
}