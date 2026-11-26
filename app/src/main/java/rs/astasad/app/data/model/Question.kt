package rs.astasad.app.data.model

import java.time.Instant

data class Question(
    val id: String,
    val authorId: String,
    val title: String,
    val body: String,
    val status: ForumContentStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val schoolId: String? = null,
    val tags: List<String> = emptyList(),
    val acceptedAnswerId: String? = null
)
