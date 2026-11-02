package com.teamsolo.studyroom.domain

/**
 * Pure input validation – no Android classes, so it can be unit-tested on the JVM (Lab 3).
 * Every function returns an error message, or null when the input is valid.
 */
object AuthValidator {

    const val MIN_PASSWORD_LENGTH = 6 // Firebase's own minimum

    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email is required"
        !EMAIL_REGEX.matches(email.trim()) -> "Enter a valid email address"
        else -> null
    }

    fun validatePassword(password: String): String? = when {
        password.isEmpty() -> "Password is required"
        password.length < MIN_PASSWORD_LENGTH ->
            "Password must have at least $MIN_PASSWORD_LENGTH characters"
        else -> null
    }

    fun validatePasswordConfirmation(password: String, confirmation: String): String? = when {
        confirmation.isEmpty() -> "Please confirm your password"
        password != confirmation -> "Passwords do not match"
        else -> null
    }
}
