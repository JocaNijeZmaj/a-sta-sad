package rs.astasad.app.data.model

import java.time.Instant

data class School(
    val id: String,
    val name: String,
    val city: String,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val website: String? = null
)
