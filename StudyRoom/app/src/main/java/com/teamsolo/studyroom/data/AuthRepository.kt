package com.teamsolo.studyroom.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Abstraction over authentication. The rest of the app never touches FirebaseAuth directly,
 * which lets us swap in a fake implementation for tests in Lab 3.
 */
interface AuthRepository {
    val currentUserEmail: String?
    fun isLoggedIn(): Boolean

    /** Emits true/false every time the signed-in user changes. */
    fun authState(): Flow<Boolean>

    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    fun logout()
}

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override val currentUserEmail: String?
        get() = auth.currentUser?.email

    override fun isLoggedIn(): Boolean = auth.currentUser != null

    override fun authState(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        auth.createUserWithEmailAndPassword(email, password).await()
        Unit
    }

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }

    override fun logout() {
        auth.signOut()
    }
}
