package rs.astasad.app.data.demo

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import rs.astasad.app.data.model.ActivityStatus
import rs.astasad.app.data.model.ActivityType
import rs.astasad.app.data.model.UserRole
import rs.astasad.app.data.repository.ActivityDetails
import rs.astasad.app.data.repository.ActivityFilter
import rs.astasad.app.data.repository.AnswerSort
import rs.astasad.app.data.repository.QuestionDetails
import rs.astasad.app.data.repository.SchoolDetails

class FakeRepositoriesTest {
    @Test
    fun activityReadsFilterPublishedResultsAndUseHalfOpenDateBounds() = runBlocking {
        val activities = DemoRepositories.forRole(UserRole.PROFESSOR).activities
        val all = activities.listActivities()
        val robotics = all.single { it.id == "demo-activity-robotics" }
        assertEquals(all.sortedBy { it.startsAt }, all)
        assertEquals(5, all.size)
        assertEquals(robotics, activities.getActivity(robotics.id))
        assertNull(activities.getActivity("demo-activity-cancelled"))
        assertTrue(activities.listActivities(ActivityFilter(status = ActivityStatus.CANCELLED)).isEmpty())

        val trimmedSearch = ActivityFilter(
            search = "  ROBOTICI ",
            schoolId = robotics.schoolId,
            type = ActivityType.WORKSHOP,
            startsFrom = robotics.startsAt,
            startsBefore = robotics.startsAt.plusSeconds(1),
            status = ActivityStatus.PUBLISHED
        )
        assertEquals(listOf(robotics), activities.listActivities(trimmedSearch))
        assertTrue(activities.listActivities(trimmedSearch.copy(startsBefore = robotics.startsAt)).isEmpty())
        assertTrue(activities.listActivities(trimmedSearch.copy(type = ActivityType.FAIR)).isEmpty())
    }

    @Test
    fun forumReadsFilterQuestionsAndSortAnswersByDateOrHelpfulness() = runBlocking {
        val demo = DemoRepositories.forRole(UserRole.HIGHSCHOOL_STUDENT)
        val all = demo.questions.listQuestions()
        assertEquals(all.sortedByDescending { it.createdAt }, all)
        assertEquals(5, all.size)
        val question = demo.questions.getQuestion("demo-question-visit")!!
        assertEquals(
            listOf(question),
            demo.questions.listQuestions(search = " LABORATORIJU ", tag = " OTVORENA VRATA ", schoolId = question.schoolId)
        )
        assertTrue(demo.questions.listQuestions(tag = "nepostojeca oznaka").isEmpty())
        assertNull(demo.questions.getQuestion("demo-question-hidden"))
        assertTrue(demo.answers.listAnswers("demo-question-hidden").isEmpty())

        val newest = demo.answers.listAnswers(question.id, AnswerSort.NEWEST)
        val helpful = demo.answers.listAnswers(question.id, AnswerSort.MOST_HELPFUL)
        assertEquals(listOf("demo-answer-visit-professor", "demo-answer-visit-student"), newest.map { it.id })
        assertEquals(newest.asReversed(), helpful)
        assertEquals(question.acceptedAnswerId, helpful.first().id)
        assertEquals(2L, demo.answerVotes.getVoteCount(helpful.first().id))
        assertEquals(0L, demo.answerVotes.getVoteCount("unknown-answer"))
    }

    @Test
    fun roleSelectionScopesOwnProfileEnrollmentsAndVotesWithoutChangingSharedReads() = runBlocking {
        val highschool = DemoRepositories.forRole(UserRole.HIGHSCHOOL_STUDENT)
        val university = DemoRepositories.forRole(UserRole.STUDENT)
        val admin = DemoRepositories.forRole(UserRole.ADMIN)
        val professor = DemoRepositories.forRole(UserRole.PROFESSOR)
        for (demo in listOf(highschool, university, admin, professor)) {
            assertEquals(demo.demoUser, demo.users.getOwnProfile())
            assertEquals(highschool.activities.listActivities(), demo.activities.listActivities())
            assertEquals(highschool.questions.listQuestions(), demo.questions.listQuestions())
        }
        assertEquals(highschool.demoUser.id, highschool.enrollments.listOwnEnrollments().single().studentId)
        assertEquals(university.demoUser.id, university.enrollments.listOwnEnrollments().single().studentId)
        assertTrue(admin.enrollments.listOwnEnrollments().isEmpty())
        assertTrue(professor.enrollments.listOwnEnrollments().isEmpty())
        assertTrue(highschool.answerVotes.hasOwnVote("demo-answer-visit-student"))
        assertFalse(university.answerVotes.hasOwnVote("demo-answer-visit-student"))
        assertTrue(university.answerVotes.hasOwnVote("demo-answer-visit-professor"))
    }

    @Test
    fun catalogAndAuthorReadsAreReproducibleAcrossInstances() = runBlocking {
        val first = DemoRepositories.forRole(UserRole.PROFESSOR)
        val second = DemoRepositories.forRole(UserRole.PROFESSOR)
        assertEquals(first.demoUser, second.demoUser)
        assertEquals(first.activities.listActivities(), second.activities.listActivities())
        assertEquals(first.schools.listSchools(), second.schools.listSchools())
        val school = first.schools.getSchool(first.demoUser.schoolId!!)!!
        val programs = first.programs.listPrograms(school.id)
        assertEquals(2, programs.size)
        assertEquals(programs.sortedBy { it.title }, programs)
        assertTrue(programs.all { it.schoolId == school.id })
        assertEquals(programs.first(), first.programs.getProgram(programs.first().id))
        assertNull(first.programs.getProgram("unknown-program"))
        assertTrue(first.programs.listPrograms("unknown-school").isEmpty())
        assertEquals(first.demoUser.displayName, first.users.getAuthorDisplayName(first.demoUser.id))
        assertNull(first.users.getAuthorDisplayName("unknown-author"))
    }

    @Test
    fun unsupportedWritesAndPrivilegedReadsFailWithoutChangingFixtures() = runBlocking {
        val demo = DemoRepositories.forRole(UserRole.HIGHSCHOOL_STUDENT)
        val activities = demo.activities.listActivities()
        val questions = demo.questions.listQuestions()
        val enrollments = demo.enrollments.listOwnEnrollments()
        val activity = activities.first()
        val details = ActivityDetails(
            activity.title, activity.description, activity.type, activity.startsAt, activity.endsAt,
            activity.location, activity.capacity, activity.costMinorUnits
        )
        val answerId = "demo-answer-visit-student"

        assertUnsupported { demo.activities.createActivity(details) }
        assertUnsupported { demo.questions.createQuestion(QuestionDetails("Novo pitanje", "Demo tekst")) }
        assertUnsupported { demo.answers.createAnswer(questions.first().id, "Demo odgovor") }
        assertUnsupported { demo.answerVotes.setOwnVote(answerId, false) }
        assertUnsupported { demo.schools.createSchool(SchoolDetails("Nova škola", "Beograd", "Demo")) }
        assertUnsupported { demo.programs.createProgram(activity.schoolId, "Novi smer", "Demo") }
        assertUnsupported { demo.users.updateOwnProfile("Novo ime", null) }
        assertUnsupported { demo.enrollments.applyToActivity(activity.id) }
        assertUnsupported { demo.users.listUsers() }
        assertUnsupported { demo.enrollments.listParticipants(activity.id) }

        assertEquals(activities, demo.activities.listActivities())
        assertEquals(questions, demo.questions.listQuestions())
        assertEquals(enrollments, demo.enrollments.listOwnEnrollments())
        assertTrue(demo.answerVotes.hasOwnVote(answerId))
    }

    private suspend fun assertUnsupported(operation: suspend () -> Any?) {
        try {
            operation()
            fail("Expected the demo operation to fail explicitly")
        } catch (exception: UnsupportedOperationException) {
            assertTrue(exception.message.orEmpty().contains("Debug demo"))
        }
    }
}
