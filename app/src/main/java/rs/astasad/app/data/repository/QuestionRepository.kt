package rs.astasad.app.data.repository

import rs.astasad.app.data.model.Question

/** Reads return visible questions. The backend resolves the author from the authenticated session. */
interface QuestionRepository {
    suspend fun listQuestions(
        search: String? = null,
        tag: String? = null,
        schoolId: String? = null
    ): List<Question>

    suspend fun getQuestion(questionId: String): Question?
    suspend fun createQuestion(details: QuestionDetails): Question
    suspend fun updateOwnQuestion(questionId: String, details: QuestionDetails)

    /** Requires question ownership and a visible answer belonging to that question. */
    suspend fun acceptAnswer(questionId: String, answerId: String)

    /** Requires administrator authorization. */
    suspend fun hideQuestion(questionId: String)
}

data class QuestionDetails(
    val title: String,
    val body: String,
    val tags: List<String> = emptyList(),
    val schoolId: String? = null
)
