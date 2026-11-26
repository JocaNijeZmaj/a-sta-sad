package rs.astasad.app.data.repository.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import rs.astasad.app.data.repository.AuthFailure
import rs.astasad.app.data.repository.AuthFailure.Reason

internal interface FirebaseAuthClient {
    fun addSessionListener(onChanged: (String?) -> Unit): () -> Unit
    suspend fun signIn(email: String, password: String)
    fun signOut()
    suspend fun sendPasswordReset(email: String)
}

private class SdkFirebaseAuthClient(private val auth: FirebaseAuth) : FirebaseAuthClient {
    override fun addSessionListener(onChanged: (String?) -> Unit): () -> Unit {
        val listener = FirebaseAuth.AuthStateListener { onChanged(it.currentUser?.uid) }
        auth.addAuthStateListener(listener)
        return { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (failure: Exception) {
            throw AuthFailure(firebaseAuthFailureReason(failure.firebaseCode(), failure.fallbackReason()))
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun sendPasswordReset(email: String) {
        try {
            auth.sendPasswordResetEmail(email).await()
        } catch (failure: Exception) {
            firebaseResetFailureReason(failure.firebaseCode(), failure.fallbackReason())?.let { reason ->
                throw AuthFailure(reason)
            }
        }
    }
}

internal fun firebaseAuthClientProvider(context: Context): () -> FirebaseAuthClient? {
    val applicationContext = context.applicationContext
    return {
        FirebaseApp.initializeApp(applicationContext)?.let { app ->
            SdkFirebaseAuthClient(FirebaseAuth.getInstance(app))
        }
    }
}

private fun Exception.firebaseCode(): String? = (this as? FirebaseAuthException)?.errorCode

private fun Exception.fallbackReason(): Reason = when (this) {
    is CancellationException -> throw this
    is FirebaseNetworkException -> Reason.NETWORK
    is FirebaseTooManyRequestsException -> Reason.RATE_LIMITED
    is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException -> Reason.INVALID_CREDENTIALS
    else -> Reason.UNEXPECTED
}

internal fun firebaseAuthFailureReason(code: String?, fallback: Reason = Reason.UNEXPECTED): Reason =
    when (code) {
        "ERROR_INVALID_EMAIL", "ERROR_WRONG_PASSWORD", "ERROR_INVALID_CREDENTIAL",
        "ERROR_INVALID_CREDENTIALS", "ERROR_INVALID_LOGIN_CREDENTIALS", "ERROR_USER_NOT_FOUND",
        "ERROR_USER_DISABLED" -> Reason.INVALID_CREDENTIALS
        "ERROR_NETWORK_REQUEST_FAILED" -> Reason.NETWORK
        "ERROR_TOO_MANY_REQUESTS" -> Reason.RATE_LIMITED
        "ERROR_OPERATION_NOT_ALLOWED", "ERROR_API_NOT_AVAILABLE" -> Reason.CONFIGURATION_UNAVAILABLE
        else -> fallback
    }

internal fun firebaseResetFailureReason(code: String?, fallback: Reason = Reason.UNEXPECTED): Reason? =
    when (code) {
        "ERROR_USER_NOT_FOUND", "ERROR_USER_DISABLED" -> null
        "ERROR_INVALID_EMAIL" -> Reason.INVALID_EMAIL
        else -> firebaseAuthFailureReason(code, fallback)
    }
