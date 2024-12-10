package com.literify.data.repository

import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authPreferences: AuthPreferences
) {

    // TODO: Implement Username/Phone Number Sign-In
    suspend fun loginWithEmailPassword(id: String, password: String): FirebaseUser {
        try {
            firebaseAuth.signInWithEmailAndPassword(id, password).await()

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

    // TODO: Implement Add User Data to Firestore
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

            val user = firebaseAuth.currentUser!!
            user.sendEmailVerification().await()

            return firebaseAuth.currentUser ?: throw Exception("Register failed")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    // TODO: Implement Find Account with Username/Phone Number
    suspend fun sendPasswordResetEmail(email: String) {
        try {
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
