package com.literify.data.repository

import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.literify.R
import com.literify.data.remote.model.UserPayload
import com.literify.data.remote.retrofit.ApiService
import com.literify.util.InputValidator.isEmailValid
import com.literify.util.StringProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authPreferences: AuthPreferences,
    private val apiService: ApiService,
    private val string: StringProvider
) {

    suspend fun signinWithEmailPassword(id: String, password: String): FirebaseUser {
        var email = id

        try {
            if (!isEmailValid(email)) {
                val response = apiService.getUserEmail(id)
                if (response.isSuccessful) {
                    email = response.body()
                        ?: throw Exception(string.getString(R.string.error_account_mismatch))
                } else {
                    when (response.code()) {
                        404 -> throw Exception(string.getString(R.string.error_account_mismatch))
                        else -> throw Exception(string.getString(R.string.error_default))
                    }
                }
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).await()

            authPreferences.setLoggedUser(id)
            authPreferences.saveCredential(id, password, null)

            return firebaseAuth.currentUser ?: throw Exception(string.getString(R.string.error_default))
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun signInWithGoogleIdToken(idToken: String?): FirebaseUser {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            firebaseAuth.signInWithCredential(credential).await()
            return firebaseAuth.currentUser
                ?: throw Exception(string.getString(R.string.error_default))
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun signupWithEmailPassword(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): FirebaseUser {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            authPreferences.setLoggedUser(email)
            authPreferences.saveCredential(email, password, null)

            val registerUser = apiService.registerUser(
                UserPayload(
                    id = firebaseAuth.currentUser!!.uid,
                    email = email,
                    username = null,
                    firstName = firstName,
                    lastName = lastName,
                    level = null
                )
            )

            if (!registerUser.isSuccessful) {
                throw Exception(string.getString(R.string.error_default))
            }

            val user = firebaseAuth.currentUser!!
            user.sendEmailVerification().await()

            return firebaseAuth.currentUser ?: throw Exception(string.getString(R.string.error_default))
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun sendPasswordResetEmail(id: String) {
        var email = id

        try {
            if (!isEmailValid(email)) {
                val response = apiService.getUserEmail(id)
                if (response.isSuccessful) {
                    email = response.body()
                        ?: throw Exception(string.getString(R.string.error_account_not_found))
                } else {
                    when (response.code()) {
                        404 -> throw Exception(string.getString(R.string.error_account_not_found))
                        else -> throw Exception(string.getString(R.string.error_default))
                    }
                }
            }

            firebaseAuth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun verifyOobCode(oobCode: String): ActionCodeResult? {
        try {
            return firebaseAuth.checkActionCode(oobCode).await()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun confirmEmailVerification(oobCode: String) {
        try {
            firebaseAuth.applyActionCode(oobCode).await()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun confirmPasswordReset(oobCode: String, newPassword: String) {
        try {
            firebaseAuth.confirmPasswordReset(oobCode, newPassword).await()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun signout() {
        try {
            firebaseAuth.signOut()
            authPreferences.clearLoggedUser()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}
