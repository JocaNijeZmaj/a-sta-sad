package rs.astasad.app.data.model

import java.time.Instant

data class UserProfile(
    val id: String,
    val displayName: String,
    val email: String,
    val role: UserRole,
    val status: AccountStatus,
    val createdAt: Instant,
    val schoolId: String? = null,
    val programId: String? = null,
    val phone: String? = null
)
