package rs.astasad.app.data.repository.firebase

import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import rs.astasad.app.data.repository.AuthFailure
import rs.astasad.app.data.repository.AuthFailure.Reason
import rs.astasad.app.data.repository.AuthRepository

class FirebaseAuthRepository internal constructor(
    private val clientProvider: () -> FirebaseAuthClient?
) : AuthRepository {
    constructor(context: Context) : this(firebaseAuthClientProvider(context))

    override fun observeSession(): Flow<String?> = callbackFlow {
        val removeListener = authOperation {
            requireClient().addSessionListener { userId -> trySend(userId) }
        }
        awaitClose { removeListener() }
    }.distinctUntilChanged()

    override suspend fun signIn(email: String, password: String) {
        val address = email.trim()
        if (address.isEmpty() || password.isEmpty()) throw AuthFailure(Reason.INVALID_CREDENTIALS)
        authOperation { requireClient().signIn(address, password) }
    }

    override suspend fun signOut() {
        authOperation { requireClient().signOut() }
    }

    override suspend fun sendPasswordReset(email: String) {
        val address = email.trim()
        if (address.isEmpty()) throw AuthFailure(Reason.INVALID_EMAIL)
        authOperation { requireClient().sendPasswordReset(address) }
    }

    override suspend fun registerHighSchoolStudent(displayName: String, email: String, password: String) {
        throw AuthFailure(Reason.REGISTRATION_UNAVAILABLE)
    }

    private fun requireClient(): FirebaseAuthClient =
        clientProvider() ?: throw AuthFailure(Reason.CONFIGURATION_UNAVAILABLE)

    private inline fun <Result> authOperation(operation: () -> Result): Result =
        try {
            operation()
        } catch (failure: CancellationException) {
            throw failure
        } catch (failure: AuthFailure) {
            throw failure
        } catch (_: Exception) {
            throw AuthFailure(Reason.UNEXPECTED)
        }
}
