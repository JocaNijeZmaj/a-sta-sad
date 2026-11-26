package rs.astasad.app.data.model

import java.time.Instant

data class Answer(
    val id: String,
    val questionId: String,
    val authorId: String,
    val body: String,
    val status: ForumContentStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)
