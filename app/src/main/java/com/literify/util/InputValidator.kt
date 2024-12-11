package com.literify.util

object InputValidator {
    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6 && password.matches(".*[a-z].*".toRegex()) &&
                password.matches(".*[A-Z].*".toRegex()) && password.matches(".*[0-9].*".toRegex())
    }
}