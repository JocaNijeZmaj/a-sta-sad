package rs.astasad.app.data.repository.firebase

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.net.HttpURLConnection
import java.net.URI
import java.util.UUID
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import rs.astasad.app.BuildConfig
import rs.astasad.app.FirebaseDevApplication
import rs.astasad.app.data.repository.AuthFailure

@RunWith(AndroidJUnit4::class)
class FirebaseAuthEmulatorTest {
    @Test
    fun missingClientConfigReturnsTypedFailure() = runBlocking {
        assumeTrue(InstrumentationRegistry.getArguments().getString("fb06NoConfig") == "true")
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertNull("This check requires an APK built without Firebase config", FirebaseOptions.fromResource(context))
        assertTrue(FirebaseApp.getApps(context).isEmpty())

        val repository = FirebaseAuthRepository(context)
        val signedIn = runCatching {
            repository.signIn("${UUID.randomUUID()}@example.invalid", UUID.randomUUID().toString())
        }.exceptionOrNull()
        val session = runCatching { withTimeout(5_000) { repository.observeSession().first() } }.exceptionOrNull()

        assertEquals(AuthFailure.Reason.CONFIGURATION_UNAVAILABLE, (signedIn as? AuthFailure)?.reason)
        assertEquals(AuthFailure.Reason.CONFIGURATION_UNAVAILABLE, (session as? AuthFailure)?.reason)
    }

    @Test
    fun signInSessionSignOutAndPasswordResetUseTheLocalEmulator() = runBlocking {
        assumeTrue(InstrumentationRegistry.getArguments().getString("fb06AuthEmulator") == "true")
        assertFalse("Cloud routing must be disabled for this test", BuildConfig.FIREBASE_DEV_CLOUD)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(context.applicationContext is FirebaseDevApplication)
        val app = FirebaseApp.getInstance()
        val projectId = checkNotNull(app.options.projectId)
        assertEquals("a-sta-sad-dev", projectId)
        val apiKey = Uri.encode(checkNotNull(app.options.apiKey))
        val email = "fb06-${UUID.randomUUID()}@example.invalid"
        val password = UUID.randomUUID().toString()
        val account = request(
            "/identitytoolkit.googleapis.com/v1/accounts:signUp?key=$apiKey",
            JSONObject().put("email", email).put("password", password).put("returnSecureToken", true)
        )
        val uid = account.getString("localId")
        val token = account.getString("idToken")
        val repository = FirebaseAuthRepository(context)
        val sessions = Channel<String?>(Channel.UNLIMITED)
        val collector = launch { repository.observeSession().collect { sessions.send(it) } }

        try {
            repository.signOut()
            assertNull(withTimeout(10_000) { sessions.receive() })

            val invalid = runCatching {
                withTimeout(15_000) { repository.signIn(email, UUID.randomUUID().toString()) }
            }.exceptionOrNull()
            assertEquals(AuthFailure.Reason.INVALID_CREDENTIALS, (invalid as? AuthFailure)?.reason)

            withTimeout(15_000) { repository.signIn(email, password) }
            assertEquals(uid, withTimeout(10_000) { sessions.receive() })
            repository.signOut()
            assertNull(withTimeout(10_000) { sessions.receive() })

            withTimeout(15_000) { repository.sendPasswordReset(email) }
            val codes = request("/emulator/v1/projects/$projectId/oobCodes").getJSONArray("oobCodes")
            assertTrue((0 until codes.length()).any { index ->
                val code = codes.getJSONObject(index)
                code.optString("email") == email && code.optString("requestType") == "PASSWORD_RESET"
            })
        } finally {
            collector.cancelAndJoin()
            try {
                repository.signOut()
            } finally {
                request(
                    "/identitytoolkit.googleapis.com/v1/accounts:delete?key=$apiKey",
                    JSONObject().put("idToken", token)
                )
            }
        }
    }

    private fun request(path: String, body: JSONObject? = null): JSONObject {
        val connection = URI.create("http://10.0.2.2:9099$path").toURL().openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = 5_000
            connection.readTimeout = 5_000
            if (body != null) {
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
            }
            assertTrue("Auth emulator returned HTTP ${connection.responseCode}", connection.responseCode in 200..299)
            connection.inputStream.bufferedReader().use { JSONObject(it.readText()) }
        } finally {
            connection.disconnect()
        }
    }
}
