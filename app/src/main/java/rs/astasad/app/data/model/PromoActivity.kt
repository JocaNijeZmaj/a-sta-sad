package rs.astasad.app.data.model

import java.time.Instant

data class PromoActivity(
    val id: String,
    val schoolId: String,
    val authorId: String,
    val title: String,
    val description: String,
    val type: ActivityType,
    val startsAt: Instant,
    val endsAt: Instant,
    val location: String,
    val capacity: Int,
    val costMinorUnits: Long,
    val status: ActivityStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val visitCount: Int? = null
)
