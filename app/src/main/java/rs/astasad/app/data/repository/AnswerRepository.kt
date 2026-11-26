package rs.astasad.app.data.repository

import rs.astasad.app.data.model.Answer

/** Reads return visible answers. The backend resolves the author from the authenticated session. */
interface AnswerRepository {
    suspend fun listAnswers(questionId: String, sort: AnswerSort = AnswerSort.NEWEST): List<Answer>
    suspend fun createAnswer(questionId: String, body: String): Answer
    suspend fun updateOwnAnswer(answerId: String, body: String)
    suspend fun deleteOwnAnswer(answerId: String)

    /** Requires administrator authorization. */
    suspend fun hideAnswer(answerId: String)
}

enum class AnswerSort {
    NEWEST,
    MOST_HELPFUL
}
