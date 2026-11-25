package rs.astasad.app.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** Emits the signed-in user's ID, or null once the session is known to be signed out. */
    fun observeSession(): Flow<String?>

    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    suspend fun sendPasswordReset(email: String)
    suspend fun registerHighSchoolStudent(displayName: String, email: String, password: String)
}
