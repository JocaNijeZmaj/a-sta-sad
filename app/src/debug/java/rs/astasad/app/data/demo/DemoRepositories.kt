package rs.astasad.app.data.demo

import rs.astasad.app.data.model.UserProfile
import rs.astasad.app.data.model.UserRole
import rs.astasad.app.data.repository.ActivityRepository
import rs.astasad.app.data.repository.AnswerRepository
import rs.astasad.app.data.repository.AnswerVoteRepository
import rs.astasad.app.data.repository.EnrollmentRepository
import rs.astasad.app.data.repository.ProgramRepository
import rs.astasad.app.data.repository.QuestionRepository
import rs.astasad.app.data.repository.SchoolRepository
import rs.astasad.app.data.repository.UserRepository

class DemoRepositories private constructor(val demoUser: UserProfile) {
    val activities: ActivityRepository = FakeActivityRepository()
    val questions: QuestionRepository = FakeQuestionRepository()
    val answers: AnswerRepository = FakeAnswerRepository()
    val answerVotes: AnswerVoteRepository = FakeAnswerVoteRepository(demoUser.id)
    val schools: SchoolRepository = FakeSchoolRepository()
    val programs: ProgramRepository = FakeProgramRepository()
    val users: UserRepository = FakeUserRepository(demoUser)
    val enrollments: EnrollmentRepository = FakeEnrollmentRepository(demoUser.id)

    companion object {
        fun forRole(role: UserRole): DemoRepositories =
            DemoRepositories(DemoData.users.first { it.role == role })
    }
}

internal const val DEMO_PAGE_SIZE = 25

internal fun unsupportedDemoOperation(): Nothing =
    throw UnsupportedOperationException("Debug demo repositories support sample reads only.")
