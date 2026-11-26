package rs.astasad.app.data.repository

/** A vote belongs to the authenticated caller; presence represents one upvote. */
interface AnswerVoteRepository {
    suspend fun getVoteCount(answerId: String): Long
    suspend fun hasOwnVote(answerId: String): Boolean

    /** Sets the desired state idempotently. False removes the caller's vote. */
    suspend fun setOwnVote(answerId: String, upvoted: Boolean)
}
