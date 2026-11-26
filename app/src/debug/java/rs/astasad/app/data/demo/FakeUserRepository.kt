package rs.astasad.app.data.demo

import rs.astasad.app.data.model.AccountStatus
import rs.astasad.app.data.model.UserProfile
import rs.astasad.app.data.repository.UserRepository

internal class FakeUserRepository(private val demoUser: UserProfile) : UserRepository {
    override suspend fun getOwnProfile(): UserProfile = demoUser
    override suspend fun getAuthorDisplayName(authorId: String): String? =
        DemoData.users.firstOrNull { it.id == authorId }?.displayName

    override suspend fun updateOwnProfile(displayName: String, phone: String?): Unit = unsupportedDemoOperation()
    override suspend fun listUsers(search: String?): List<UserProfile> = unsupportedDemoOperation()
    override suspend fun getUser(userId: String): UserProfile? = unsupportedDemoOperation()
    override suspend fun requestAccountStatusChange(userId: String, status: AccountStatus): Unit =
        unsupportedDemoOperation()
}
