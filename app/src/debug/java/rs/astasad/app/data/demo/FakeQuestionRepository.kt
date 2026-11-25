package rs.astasad.app.data.demo

import java.util.Locale
import rs.astasad.app.data.model.Question
import rs.astasad.app.data.repository.QuestionDetails
import rs.astasad.app.data.repository.QuestionRepository

internal class FakeQuestionRepository : QuestionRepository {
    override suspend fun listQuestions(search: String?, tag: String?, schoolId: String?): List<Question> {
        val normalizedTag = tag?.trim()?.lowercase(Locale.ROOT)?.takeIf { it.isNotEmpty() }
        val loaded = DemoData.publishedQuestions
            .filter { question ->
                (schoolId == null || question.schoolId == schoolId) &&
                    (normalizedTag == null || normalizedTag in question.tags)
            }
            .sortedWith(compareByDescending(Question::createdAt).thenByDescending(Question::id))
            .take(DEMO_PAGE_SIZE)

        val query = search.orEmpty().trim()
        return loaded.filter {
            it.title.contains(query, ignoreCase = true) || it.body.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getQuestion(questionId: String): Question? =
        DemoData.publishedQuestions.firstOrNull { it.id == questionId }

    override suspend fun createQuestion(details: QuestionDetails): Question = unsupportedDemoOperation()
    override suspend fun updateOwnQuestion(questionId: String, details: QuestionDetails): Unit =
        unsupportedDemoOperation()

    override suspend fun acceptAnswer(questionId: String, answerId: String): Unit = unsupportedDemoOperation()
    override suspend fun hideQuestion(questionId: String): Unit = unsupportedDemoOperation()
}
