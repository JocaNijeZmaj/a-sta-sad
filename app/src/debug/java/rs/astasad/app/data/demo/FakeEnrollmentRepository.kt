package rs.astasad.app.data.demo

import rs.astasad.app.data.model.Enrollment
import rs.astasad.app.data.repository.EnrollmentRepository

internal class FakeEnrollmentRepository(private val demoUserId: String) : EnrollmentRepository {
    override suspend fun listOwnEnrollments(): List<Enrollment> =
        DemoData.enrollments.filter { it.studentId == demoUserId }
            .sortedWith(compareByDescending(Enrollment::updatedAt).thenByDescending(Enrollment::id))
            .take(DEMO_PAGE_SIZE)

    override suspend fun applyToActivity(activityId: String): Enrollment = unsupportedDemoOperation()
    override suspend fun cancelOwnEnrollment(activityId: String, reason: String): Unit = unsupportedDemoOperation()
    override suspend fun completeOwnEnrollment(activityId: String, note: String): Unit = unsupportedDemoOperation()

    override suspend fun listParticipants(activityId: String): List<Enrollment> = unsupportedDemoOperation()
    override suspend fun addParticipant(activityId: String, studentId: String): Enrollment = unsupportedDemoOperation()
    override suspend fun removeParticipant(activityId: String, studentId: String): Unit = unsupportedDemoOperation()
}
