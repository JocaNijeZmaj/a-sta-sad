package rs.astasad.app.data.model

import java.time.Instant

data class Enrollment(
    val id: String,
    val activityId: String,
    val studentId: String,
    val status: EnrollmentStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val cancellationReason: String? = null,
    val completionNote: String? = null
)
