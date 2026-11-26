package rs.astasad.app.data.model

import java.time.Instant

data class AnswerVote(
    val id: String,
    val answerId: String,
    val userId: String,
    val createdAt: Instant
) {
    val value: Int get() = 1
}
