package rs.astasad.app.data.repository

import rs.astasad.app.data.model.AccountStatus
import rs.astasad.app.data.model.UserProfile

/** Self-service operations resolve the user from the authenticated session. */
interface UserRepository {
    suspend fun getOwnProfile(): UserProfile?
    suspend fun updateOwnProfile(displayName: String, phone: String?)

    /** Returns a display name through an authorized public projection. */
    suspend fun getAuthorDisplayName(authorId: String): String?

    /** Requires administrator authorization. */
    suspend fun listUsers(search: String? = null): List<UserProfile>

    /** Requires administrator authorization. */
    suspend fun getUser(userId: String): UserProfile?

    /** Requires a trusted administrator operation. */
    suspend fun requestAccountStatusChange(userId: String, status: AccountStatus)
}
