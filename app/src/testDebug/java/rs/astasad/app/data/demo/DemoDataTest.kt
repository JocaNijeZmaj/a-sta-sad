package rs.astasad.app.data.demo

import java.time.Instant
import java.util.Base64
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import rs.astasad.app.data.model.EnrollmentStatus
import rs.astasad.app.data.model.ForumContentStatus
import rs.astasad.app.data.model.UserRole

class DemoDataTest {
    @Test
    fun fixturesCoverAllRolesAndUseStableIdentifiersAndDates() {
        assertEquals(UserRole.entries.toSet(), DemoData.users.map { it.role }.toSet())
        assertTrue(DemoData.users.all { it.email.endsWith("@example.invalid") && it.phone == null })
        assertEquals(
            listOf("demo-school-vidik", "demo-school-most", "demo-school-iskra"),
            DemoData.schools.map { it.id }
        )
        assertEquals(6, DemoData.programs.size)
        assertEquals(5, DemoData.publishedActivities.size)
        assertEquals(5, DemoData.publishedQuestions.size)
        assertEquals(Instant.parse("2026-10-22T09:00:00Z"), DemoData.publishedActivities.first().startsAt)

        val ids = DemoData.users.map { it.id } + DemoData.schools.map { it.id } +
            DemoData.programs.map { it.id } + DemoData.activities.map { it.id } +
            DemoData.questions.map { it.id } + DemoData.answers.map { it.id } +
            DemoData.enrollments.map { it.id } + DemoData.votes.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun programsUsersAndActivitiesReferenceTheCorrectSchools() {
        val schools = DemoData.schools.associateBy { it.id }
        val programs = DemoData.programs.associateBy { it.id }
        val users = DemoData.users.associateBy { it.id }
        DemoData.programs.forEach { assertNotNull(schools[it.schoolId]) }
        DemoData.users.forEach { user ->
            user.schoolId?.let { assertNotNull(schools[it]) }
            user.programId?.let { assertEquals(user.schoolId, programs.getValue(it).schoolId) }
            if (user.role == UserRole.PROFESSOR) assertNotNull(user.schoolId)
        }
        DemoData.activities.forEach { activity ->
            val author = users.getValue(activity.authorId)
            assertNotNull(schools[activity.schoolId])
            assertEquals(UserRole.PROFESSOR, author.role)
            assertEquals(activity.schoolId, author.schoolId)
            assertTrue(activity.startsAt < activity.endsAt)
            assertTrue(activity.capacity > 0 && activity.costMinorUnits >= 0)
        }
    }

    @Test
    fun forumAuthorsAcceptedAnswersAndVotesMatchTheirReferences() {
        val users = DemoData.users.associateBy { it.id }
        val schoolIds = DemoData.schools.map { it.id }.toSet()
        val questions = DemoData.questions.associateBy { it.id }
        val answers = DemoData.answers.associateBy { it.id }
        assertEquals(
            setOf(UserRole.STUDENT, UserRole.PROFESSOR),
            DemoData.visibleAnswers.map { users.getValue(it.authorId).role }.toSet()
        )
        DemoData.questions.forEach { question ->
            assertEquals(UserRole.HIGHSCHOOL_STUDENT, users.getValue(question.authorId).role)
            question.schoolId?.let { assertTrue(it in schoolIds) }
            question.acceptedAnswerId?.let {
                val accepted = answers.getValue(it)
                assertEquals(question.id, accepted.questionId)
                assertEquals(ForumContentStatus.PUBLISHED, accepted.status)
                assertTrue(accepted.createdAt <= question.updatedAt)
            }
        }
        DemoData.answers.forEach { answer ->
            assertTrue(answer.createdAt >= questions.getValue(answer.questionId).createdAt)
        }
        val visibleAnswerIds = DemoData.visibleAnswers.map { it.id }.toSet()
        val votePairs = DemoData.votes.map { vote ->
            assertTrue(vote.answerId in visibleAnswerIds)
            assertNotNull(users[vote.userId])
            assertTrue(vote.createdAt >= answers.getValue(vote.answerId).createdAt)
            assertEquals(1, vote.value)
            assertPairId(vote.id, vote.answerId, vote.userId)
            vote.answerId to vote.userId
        }
        assertEquals(votePairs.size, votePairs.toSet().size)
    }

    @Test
    fun enrollmentsUseCanonicalKeysAndReferenceEligibleParticipants() {
        val users = DemoData.users.associateBy { it.id }
        val activities = DemoData.activities.associateBy { it.id }
        val eligibleRoles = setOf(UserRole.HIGHSCHOOL_STUDENT, UserRole.STUDENT)
        assertEquals(eligibleRoles, DemoData.enrollments.map { users.getValue(it.studentId).role }.toSet())
        val pairs = DemoData.enrollments.map { enrollment ->
            val activity = activities.getValue(enrollment.activityId)
            assertTrue(enrollment.createdAt < activity.startsAt)
            assertTrue(enrollment.updatedAt >= enrollment.createdAt)
            when (enrollment.status) {
                EnrollmentStatus.ACTIVE -> assertEquals(null, enrollment.cancellationReason)
                EnrollmentStatus.CANCELLED -> assertTrue(!enrollment.cancellationReason.isNullOrBlank())
                EnrollmentStatus.COMPLETED -> {
                    assertTrue(!enrollment.completionNote.isNullOrBlank())
                    assertTrue(enrollment.updatedAt >= activity.endsAt)
                }
            }
            assertPairId(enrollment.id, activity.id, enrollment.studentId)
            activity.id to enrollment.studentId
        }
        assertEquals(pairs.size, pairs.toSet().size)
    }

    private fun assertPairId(id: String, parentId: String, userId: String) {
        val parts = id.split(".")
        assertEquals(3, parts.size)
        assertEquals("v1", parts[0])
        assertTrue(parts.drop(1).all { it.matches(Regex("[A-Za-z0-9_-]+")) })
        val decoder = Base64.getUrlDecoder()
        assertEquals(parentId, decoder.decode(parts[1]).toString(Charsets.UTF_8))
        assertEquals(userId, decoder.decode(parts[2]).toString(Charsets.UTF_8))
    }
}
