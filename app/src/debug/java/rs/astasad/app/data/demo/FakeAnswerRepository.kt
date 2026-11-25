package rs.astasad.app.data.demo

import rs.astasad.app.data.model.Answer
import rs.astasad.app.data.repository.AnswerRepository
import rs.astasad.app.data.repository.AnswerSort

internal class FakeAnswerRepository : AnswerRepository {
    override suspend fun listAnswers(questionId: String, sort: AnswerSort): List<Answer> {
        val newest = compareByDescending(Answer::createdAt).thenByDescending(Answer::id)
        val order = when (sort) {
            AnswerSort.NEWEST -> newest
            AnswerSort.MOST_HELPFUL -> compareByDescending<Answer> { answer ->
                DemoData.votes.count { it.answerId == answer.id }
            }.then(newest)
        }
        return DemoData.visibleAnswers.filter { it.questionId == questionId }.sortedWith(order).take(DEMO_PAGE_SIZE)
    }

    override suspend fun createAnswer(questionId: String, body: String): Answer = unsupportedDemoOperation()
    override suspend fun updateOwnAnswer(answerId: String, body: String): Unit = unsupportedDemoOperation()
    override suspend fun deleteOwnAnswer(answerId: String): Unit = unsupportedDemoOperation()
    override suspend fun hideAnswer(answerId: String): Unit = unsupportedDemoOperation()
}
