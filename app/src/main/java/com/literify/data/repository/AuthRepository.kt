package com.literify.data.repository

import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.literify.data.remote.model.UserPayload
import com.literify.data.remote.retrofit.ApiService
import com.literify.util.InputValidator.isEmailValid
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authPreferences: AuthPreferences,
    private val apiService: ApiService
) {

    suspend fun loginWithEmailPassword(id: String, password: String): FirebaseUser {
        var email = id

        try {
            if (!isEmailValid(email)) {
                val response = apiService.getUserEmail(id)
                if (response.isSuccessful) {
                    email = response.body() ?: throw Exception("Email not found in response")
                } else {
                    when (response.code()) {
                        404 -> throw Exception("Username $id not found")
                        else -> throw Exception("Error fetching email: ${response.message()}")
                    }
                }
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).await()

            authPreferences.setLoggedUser(id)
            authPreferences.saveCredential(id, password, null)

            return firebaseAuth.currentUser ?: throw Exception("Login failed")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun loginWithGoogleIdToken(idToken: String?): FirebaseUser {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            firebaseAuth.signInWithCredential(credential).await()
            return firebaseAuth.currentUser ?: throw Exception("Login with Google failed")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun registerWithEmailPassword(
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
                throw Exception("Failed to register user: ${registerUser.message()}")
            }

            val user = firebaseAuth.currentUser!!
            user.sendEmailVerification().await()

            return firebaseAuth.currentUser ?: throw Exception("Register failed")
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
                    email = response.body() ?: throw Exception("Email not found in response")
                } else {
                    when (response.code()) {
                        404 -> throw Exception("Username $id not found")
                        else -> throw Exception("Error fetching email: ${response.message()}")
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

    suspend fun logout(): FirebaseUser {
        try {
            firebaseAuth.signOut()
            authPreferences.clearLoggedUser()

            return firebaseAuth.currentUser ?: throw Exception("Logout failed")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}
