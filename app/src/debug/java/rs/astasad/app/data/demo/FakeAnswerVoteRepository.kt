package rs.astasad.app.data.demo

import rs.astasad.app.data.repository.AnswerVoteRepository

internal class FakeAnswerVoteRepository(private val demoUserId: String) : AnswerVoteRepository {
    override suspend fun getVoteCount(answerId: String): Long =
        visibleVotes(answerId).size.toLong()

    override suspend fun hasOwnVote(answerId: String): Boolean =
        visibleVotes(answerId).any { it.userId == demoUserId }

    override suspend fun setOwnVote(answerId: String, upvoted: Boolean): Unit = unsupportedDemoOperation()

    private fun visibleVotes(answerId: String) =
        if (DemoData.visibleAnswers.any { it.id == answerId }) {
            DemoData.votes.filter { it.answerId == answerId }
        } else {
            emptyList()
        }
}
