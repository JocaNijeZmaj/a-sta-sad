package rs.astasad.app.data.repository.firebase

import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test
import rs.astasad.app.data.repository.AuthFailure
import rs.astasad.app.data.repository.AuthFailure.Reason

class FirebaseAuthRepositoryTest {
    private val email = "${UUID.randomUUID()}@example.invalid"
    private val password = " ${UUID.randomUUID()} "
    private val client = FakeClient()
    private val repository = FirebaseAuthRepository { client }

    @Test
    fun signInWaitsForCompletionAndPreservesPasswordWhitespace() = runBlocking {
        val entered = CompletableDeferred<Unit>()
        val finish = CompletableDeferred<Unit>()
        client.signInAction = {
            entered.complete(Unit)
            finish.await()
        }

        val signIn = async { repository.signIn("  $email ", password) }
        try {
            withTimeout(2_000) { entered.await() }
            assertFalse(signIn.isCompleted)
            assertEquals(email to password, client.submittedCredentials)
        } finally {
            finish.complete(Unit)
        }
        signIn.await()
    }

    @Test
    fun sessionFlowEmitsConfirmedStateDeduplicatesAndRemovesListener() = runBlocking {
        val sessions = Channel<String?>(Channel.UNLIMITED)
        val collector = launch { repository.observeSession().collect { sessions.send(it) } }

        try {
            withTimeout(2_000) { client.listenerRegistered.await() }
            assertFalse(sessions.tryReceive().isSuccess)
            client.publishSession(null)
            assertNull(withTimeout(2_000) { sessions.receive() })
            client.signInAction = { client.publishSession("fixture-uid") }
            repository.signIn(email, password)
            assertEquals("fixture-uid", withTimeout(2_000) { sessions.receive() })
            client.publishSession("fixture-uid")
            repository.signOut()
            assertEquals(1, client.signOutCalls)
            assertNull(withTimeout(2_000) { sessions.receive() })
        } finally {
            collector.cancelAndJoin()
        }
        assertEquals(1, client.removedListeners)
    }

    @Test
    fun signInMapsErrorCodesAndPropagatesTypedFailures() = runBlocking {
        val failures = listOf(
            "ERROR_WRONG_PASSWORD" to Reason.INVALID_CREDENTIALS,
            "ERROR_USER_NOT_FOUND" to Reason.INVALID_CREDENTIALS,
            "ERROR_INVALID_LOGIN_CREDENTIALS" to Reason.INVALID_CREDENTIALS,
            "ERROR_NETWORK_REQUEST_FAILED" to Reason.NETWORK,
            "ERROR_TOO_MANY_REQUESTS" to Reason.RATE_LIMITED,
            "ERROR_OPERATION_NOT_ALLOWED" to Reason.CONFIGURATION_UNAVAILABLE,
            "ERROR_UNKNOWN" to Reason.UNEXPECTED
        )
        for ((code, reason) in failures) {
            val mapped = firebaseAuthFailureReason(code)
            assertEquals(reason, mapped)
            client.signInAction = { throw AuthFailure(mapped) }
            expectFailure(reason) { repository.signIn(email, password) }
        }
        assertEquals(Reason.NETWORK, firebaseAuthFailureReason(null, Reason.NETWORK))
        client.signInAction = { error("SDK detail") }
        expectFailure(Reason.UNEXPECTED) { repository.signIn(email, password) }
    }

    @Test
    fun missingConfigurationIsLazyAndDoesNotEmitSignedOut() = runBlocking {
        var lookups = 0
        val unconfigured = FirebaseAuthRepository {
            lookups++
            null
        }
        val session = unconfigured.observeSession()
        assertEquals(0, lookups)

        expectFailure(Reason.CONFIGURATION_UNAVAILABLE) { session.first() }
        expectFailure(Reason.CONFIGURATION_UNAVAILABLE) { unconfigured.signIn(email, password) }
        expectFailure(Reason.CONFIGURATION_UNAVAILABLE) { unconfigured.signOut() }
        expectFailure(Reason.CONFIGURATION_UNAVAILABLE) { unconfigured.sendPasswordReset(email) }
        assertEquals(4, lookups)
    }

    @Test
    fun passwordResetDoesNotRevealWhetherTheAccountExists() = runBlocking {
        repository.sendPasswordReset(" $email ")
        assertEquals(email, client.resetAddress)
        for (code in listOf("ERROR_USER_NOT_FOUND", "ERROR_USER_DISABLED")) {
            assertNull(firebaseResetFailureReason(code))
            repository.sendPasswordReset(email)
        }
        assertEquals(Reason.INVALID_EMAIL, firebaseResetFailureReason("ERROR_INVALID_EMAIL"))
        assertEquals(Reason.NETWORK, firebaseResetFailureReason("ERROR_NETWORK_REQUEST_FAILED"))
        client.resetAction = { throw AuthFailure(Reason.INVALID_EMAIL) }
        expectFailure(Reason.INVALID_EMAIL) { repository.sendPasswordReset(email) }
        client.resetAction = { throw AuthFailure(Reason.NETWORK) }
        expectFailure(Reason.NETWORK) { repository.sendPasswordReset(email) }
    }

    @Test
    fun cancellationPropagatesUnchanged() = runBlocking {
        val cancellation = CancellationException("caller cancelled")
        client.signInAction = { throw cancellation }
        client.resetAction = { throw cancellation }

        val signInFailure = runCatching { repository.signIn(email, password) }.exceptionOrNull()
        val resetFailure = runCatching { repository.sendPasswordReset(email) }.exceptionOrNull()
        assertSame(cancellation, signInFailure)
        assertSame(cancellation, resetFailure)
    }

    @Test
    fun emptyInputAndUnsupportedRegistrationDoNotCallFirebase() = runBlocking {
        val unused = FirebaseAuthRepository { error("Invalid or unsupported requests must not access Firebase") }
        expectFailure(Reason.INVALID_CREDENTIALS) { unused.signIn(" ", password) }
        expectFailure(Reason.INVALID_CREDENTIALS) { unused.signIn(email, "") }
        expectFailure(Reason.INVALID_EMAIL) { unused.sendPasswordReset(" ") }
        expectFailure(Reason.REGISTRATION_UNAVAILABLE) { unused.registerHighSchoolStudent("Fixture", email, password) }
    }

    private suspend fun expectFailure(reason: Reason, operation: suspend () -> Any?) {
        val failure = runCatching { operation() }.exceptionOrNull() as? AuthFailure
        assertEquals(reason, failure?.reason)
        assertEquals(reason.name, failure?.message)
        assertNull(failure?.cause)
    }

    private class FakeClient : FirebaseAuthClient {
        val listenerRegistered = CompletableDeferred<Unit>()
        var removedListeners = 0
        var signOutCalls = 0
        var submittedCredentials: Pair<String, String>? = null
        var resetAddress: String? = null
        var signInAction: suspend () -> Unit = {}
        var resetAction: suspend () -> Unit = {}
        private var listener: ((String?) -> Unit)? = null

        override fun addSessionListener(onChanged: (String?) -> Unit): () -> Unit {
            listener = onChanged
            listenerRegistered.complete(Unit)
            return {
                listener = null
                removedListeners++
            }
        }

        fun publishSession(userId: String?) {
            checkNotNull(listener)(userId)
        }

        override suspend fun signIn(email: String, password: String) {
            submittedCredentials = email to password
            signInAction()
        }

        override fun signOut() {
            signOutCalls++
            publishSession(null)
        }

        override suspend fun sendPasswordReset(email: String) {
            resetAddress = email
            resetAction()
        }
    }
}
