package rs.astasad.app.data.repository

import rs.astasad.app.data.model.Enrollment

/**
 * Self-service operations resolve the student from the authenticated session.
 * The backend validates ownership, eligibility, capacity, and status transitions.
 */
interface EnrollmentRepository {
    suspend fun listOwnEnrollments(): List<Enrollment>
    suspend fun applyToActivity(activityId: String): Enrollment
    suspend fun cancelOwnEnrollment(activityId: String, reason: String)
    suspend fun completeOwnEnrollment(activityId: String, note: String)

    /** Participant management requires admin or school-scoped professor authorization. */
    suspend fun listParticipants(activityId: String): List<Enrollment>
    suspend fun addParticipant(activityId: String, studentId: String): Enrollment
    suspend fun removeParticipant(activityId: String, studentId: String)
}
