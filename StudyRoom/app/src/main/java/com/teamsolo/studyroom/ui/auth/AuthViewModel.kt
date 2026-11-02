package com.teamsolo.studyroom.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.teamsolo.studyroom.data.AuthRepository
import com.teamsolo.studyroom.data.FirebaseAuthRepository
import com.teamsolo.studyroom.domain.AuthValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /** Navigation observes this – screens never decide where to go after login. */
    val isLoggedIn: StateFlow<Boolean> = repository.authState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, repository.isLoggedIn())

    val currentUserEmail: String?
        get() = repository.currentUserEmail

    fun startsLoggedIn(): Boolean = repository.isLoggedIn()

    // --- Form input ---------------------------------------------------------

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, emailError = null, generalError = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null, generalError = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, generalError = null) }

    /** Call when switching between Login and Register so old errors don't leak across screens. */
    fun resetForm() {
        _uiState.value = AuthUiState()
    }

    // --- Actions ------------------------------------------------------------

    fun login() {
        val s = _uiState.value
        val emailError = AuthValidator.validateEmail(s.email)
        val passwordError = if (s.password.isEmpty()) "Password is required" else null
        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }
        submit { repository.login(s.email.trim(), s.password) }
    }

    fun register() {
        val s = _uiState.value
        val emailError = AuthValidator.validateEmail(s.email)
        val passwordError = AuthValidator.validatePassword(s.password)
        val confirmError = AuthValidator.validatePasswordConfirmation(s.password, s.confirmPassword)
        if (emailError != null || passwordError != null || confirmError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }
        submit { repository.register(s.email.trim(), s.password) }
    }

    fun logout() {
        repository.logout()
        resetForm()
    }

    private fun submit(call: suspend () -> Result<Unit>) {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, generalError = null) }
        viewModelScope.launch {
            val result = call()
            _uiState.update { state ->
                result.fold(
                    onSuccess = { AuthUiState() }, // clear form; navigation reacts to isLoggedIn
                    onFailure = { e -> state.copy(isLoading = false, generalError = mapError(e)) }
                )
            }
        }
    }

    /** Never show raw exception text to the user. */
    private fun mapError(e: Throwable): String = when (e) {
        is FirebaseAuthUserCollisionException -> "An account with this email already exists"
        is FirebaseAuthWeakPasswordException -> "This password is too weak"
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password"
        is FirebaseNetworkException -> "No internet connection. Try again."
        is FirebaseTooManyRequestsException -> "Too many attempts. Please wait a moment."
        else -> "Something went wrong. Please try again."
    }
}
