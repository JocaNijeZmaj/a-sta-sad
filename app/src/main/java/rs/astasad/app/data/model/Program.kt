package rs.astasad.app.data.model

import java.time.Instant

data class Program(
    val id: String,
    val schoolId: String,
    val title: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
