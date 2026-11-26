package rs.astasad.app.data.model

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainModelsTest {
    private val timestamp = Instant.parse("2026-09-15T08:00:00Z")

    @Test
    fun profileAndSchoolDoNotInventOptionalDetails() {
        val profile = UserProfile(
            id = "student-1",
            displayName = "Test Student",
            email = "student@example.invalid",
            role = UserRole.STUDENT,
            status = AccountStatus.ACTIVE,
            createdAt = timestamp
        )
        val school = School(
            id = "school-1",
            name = "Test School",
            city = "Belgrade",
            description = "Example school",
            createdAt = timestamp,
            updatedAt = timestamp
        )

        assertNull(profile.schoolId)
        assertNull(profile.programId)
        assertNull(profile.phone)
        assertNull(school.website)
    }

    @Test
    fun questionAndEnrollmentDefaultToNoOptionalDetails() {
        val question = Question(
            id = "question-1",
            authorId = "highschool-student-1",
            title = "Admissions",
            body = "When do applications open?",
            status = ForumContentStatus.PUBLISHED,
            createdAt = timestamp,
            updatedAt = timestamp
        )
        val enrollment = Enrollment(
            id = "activity-1_student-1",
            activityId = "activity-1",
            studentId = "student-1",
            status = EnrollmentStatus.ACTIVE,
            createdAt = timestamp,
            updatedAt = timestamp
        )

        assertTrue(question.tags.isEmpty())
        assertNull(question.schoolId)
        assertNull(question.acceptedAnswerId)
        assertNull(enrollment.cancellationReason)
        assertNull(enrollment.completionNote)
    }

    @Test
    fun activityRecordConstructionDoesNotRunWriteValidation() {
        val activity = PromoActivity(
            id = "activity-1",
            schoolId = "school-1",
            authorId = "professor-1",
            title = "Workshop",
            description = "Example workshop",
            type = ActivityType.WORKSHOP,
            startsAt = timestamp,
            endsAt = timestamp.minusSeconds(60),
            location = "Test School",
            capacity = 0,
            costMinorUnits = -1L,
            status = ActivityStatus.PUBLISHED,
            createdAt = timestamp,
            updatedAt = timestamp
        )

        assertTrue(activity.endsAt.isBefore(activity.startsAt))
        assertEquals(0, activity.capacity)
        assertEquals(-1L, activity.costMinorUnits)
        assertNull(activity.visitCount)
    }

    @Test
    fun answerVotesAlwaysRepresentOneUpvote() {
        val vote = AnswerVote(
            id = "answer-1_student-1",
            answerId = "answer-1",
            userId = "student-1",
            createdAt = timestamp
        )

        assertEquals(1, vote.value)
        assertEquals(1, vote.copy(createdAt = timestamp.plusSeconds(60)).value)
    }

    @Test
    fun roleAndStatusNamesMatchTheSchemaContract() {
        assertEquals(listOf("ADMIN", "PROFESSOR", "STUDENT", "HIGHSCHOOL_STUDENT"), UserRole.entries.map { it.name })
        assertEquals(listOf("ACTIVE", "BANNED"), AccountStatus.entries.map { it.name })
        assertEquals(listOf("PUBLISHED", "CANCELLED", "REMOVED"), ActivityStatus.entries.map { it.name })
        assertEquals(listOf("ACTIVE", "CANCELLED", "COMPLETED"), EnrollmentStatus.entries.map { it.name })
        assertEquals(listOf("PUBLISHED", "HIDDEN"), ForumContentStatus.entries.map { it.name })
        assertEquals(listOf("OPEN", "RESOLVED", "DISMISSED"), ReportStatus.entries.map { it.name })
        assertEquals(listOf("FAIR", "SCHOOL_TOUR", "WORKSHOP"), ActivityType.entries.map { it.name })
        assertEquals(listOf("QUESTION", "ANSWER"), ReportTargetType.entries.map { it.name })
    }
}
