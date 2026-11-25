package rs.astasad.app.data.demo

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64
import rs.astasad.app.data.model.AccountStatus
import rs.astasad.app.data.model.ActivityStatus
import rs.astasad.app.data.model.ActivityType
import rs.astasad.app.data.model.Answer
import rs.astasad.app.data.model.AnswerVote
import rs.astasad.app.data.model.Enrollment
import rs.astasad.app.data.model.EnrollmentStatus
import rs.astasad.app.data.model.ForumContentStatus
import rs.astasad.app.data.model.Program
import rs.astasad.app.data.model.PromoActivity
import rs.astasad.app.data.model.Question
import rs.astasad.app.data.model.School
import rs.astasad.app.data.model.UserProfile
import rs.astasad.app.data.model.UserRole

internal object DemoData {
    private const val VIDIK = "demo-school-vidik"
    private const val MOST = "demo-school-most"
    private const val ISKRA = "demo-school-iskra"
    private const val ADMIN = "demo-admin"
    private const val VIDIK_PROFESSOR = "demo-professor-vidik"
    private const val MOST_PROFESSOR = "demo-professor-most"
    private const val ISKRA_PROFESSOR = "demo-professor-iskra"
    private const val STUDENT = "demo-student"
    private const val HIGHSCHOOL_STUDENT = "demo-highschool-student"
    private const val SECOND_HIGHSCHOOL_STUDENT = "demo-highschool-student-2"
    private const val OPEN_DAY = "demo-activity-open-day"
    private const val ROBOTICS = "demo-activity-robotics"
    private const val DESIGN = "demo-activity-design"
    private const val FAIR = "demo-activity-fair"
    private const val MEDIA = "demo-activity-media"
    private const val CANCELLED = "demo-activity-cancelled"
    private const val VISIT_QUESTION = "demo-question-visit"
    private const val ROBOTICS_QUESTION = "demo-question-robotics"
    private const val DESIGN_QUESTION = "demo-question-design"
    private const val FAIR_QUESTION = "demo-question-fair"
    private const val MEDIA_QUESTION = "demo-question-media"
    private const val HIDDEN_QUESTION = "demo-question-hidden"
    private const val VISIT_STUDENT_ANSWER = "demo-answer-visit-student"
    private const val VISIT_PROFESSOR_ANSWER = "demo-answer-visit-professor"
    private const val ROBOTICS_ANSWER = "demo-answer-robotics"
    private const val DESIGN_ANSWER = "demo-answer-design"
    private const val FAIR_ANSWER = "demo-answer-fair"
    private const val MEDIA_ANSWER = "demo-answer-media"

    private val epoch = Instant.parse("2026-10-01T09:00:00Z")

    val schools = listOf(
        School(VIDIK, "Demo akademija Vidik", "Beograd", "Programi iz računarstva i tehnike.", epoch, epoch),
        School(MOST, "Demo akademija Most", "Novi Sad", "Programi iz dizajna i komunikacija.", epoch, epoch),
        School(ISKRA, "Demo akademija Iskra", "Niš", "Programi iz medija i primenjenih tehnologija.", epoch, epoch)
    )

    val programs = listOf(
        Program("demo-program-software", VIDIK, "Softversko inženjerstvo", "Razvoj aplikacija i timski rad.", epoch, epoch),
        Program("demo-program-automation", VIDIK, "Automatika", "Senzori i upravljanje uređajima.", epoch, epoch),
        Program("demo-program-design", MOST, "Vizuelne komunikacije", "Dizajn za digitalne i štampane medije.", epoch, epoch),
        Program("demo-program-web", MOST, "Veb dizajn", "Dizajn pristupačnih veb stranica.", epoch, epoch),
        Program("demo-program-media", ISKRA, "Digitalni mediji", "Obrada slike, zvuka i videa.", epoch, epoch),
        Program("demo-program-networks", ISKRA, "Računarske mreže", "Osnove mrežne infrastrukture.", epoch, epoch)
    )

    val users = listOf(
        user(ADMIN, "Demo administrator", UserRole.ADMIN),
        user(VIDIK_PROFESSOR, "Demo profesor računarstva", UserRole.PROFESSOR, VIDIK),
        user(MOST_PROFESSOR, "Demo profesor dizajna", UserRole.PROFESSOR, MOST),
        user(ISKRA_PROFESSOR, "Demo profesor medija", UserRole.PROFESSOR, ISKRA),
        user(STUDENT, "Demo student softvera", UserRole.STUDENT, VIDIK, "demo-program-software"),
        user(HIGHSCHOOL_STUDENT, "Demo srednjoškolac", UserRole.HIGHSCHOOL_STUDENT),
        user(SECOND_HIGHSCHOOL_STUDENT, "Demo srednjoškolka", UserRole.HIGHSCHOOL_STUDENT)
    )

    val activities = listOf(
        activity(
            OPEN_DAY, VIDIK, VIDIK_PROFESSOR, "Otvorena vrata akademije",
            "Obiđi laboratorije i razgovaraj sa studentima o njihovim projektima.",
            ActivityType.SCHOOL_TOUR, 21, "Laboratorija Vidik", 30, visitCount = 24
        ),
        activity(
            ROBOTICS, VIDIK, VIDIK_PROFESSOR, "Prvi koraci u robotici",
            "U malim timovima poveži senzor i isprogramiraj jednostavan robot.",
            ActivityType.WORKSHOP, 63, "Radionica Vidik", 16
        ),
        activity(
            DESIGN, MOST, MOST_PROFESSOR, "Radionica vizuelnog dizajna",
            "Napravi plakat i saznaj kako izgleda portfolio za prijemni.",
            ActivityType.WORKSHOP, 65, "Studio Most", 18
        ),
        activity(
            FAIR, MOST, MOST_PROFESSOR, "Sajam studijskih programa",
            "Uporedi smerove i pitaj studente šta rade na vežbama.",
            ActivityType.FAIR, 70, "Hol akademije Most", 80
        ),
        activity(
            MEDIA, ISKRA, ISKRA_PROFESSOR, "Obilazak medijskog studija",
            "Pogledaj opremu za snimanje i probaj osnovnu obradu zvuka.",
            ActivityType.SCHOOL_TOUR, 72, "Medijski studio Iskra", 20
        ),
        activity(
            CANCELLED, ISKRA, ISKRA_PROFESSOR, "Uvod u računarske mreže",
            "Planirana demonstracija je otkazana zbog nedostupnosti laboratorije.",
            ActivityType.WORKSHOP, 66, "Laboratorija Iskra", 12,
            status = ActivityStatus.CANCELLED
        )
    )

    val questions = listOf(
        question(
            VISIT_QUESTION, HIGHSCHOOL_STUDENT, VIDIK, "Šta mogu da vidim na otvorenim vratima?",
            "Zanima me da li mogu da vidim laboratoriju i pitam studente o prvoj godini.",
            listOf("otvorena vrata", "studiranje"), 5, VISIT_STUDENT_ANSWER
        ),
        question(
            ROBOTICS_QUESTION, HIGHSCHOOL_STUDENT, VIDIK, "Da li je radionica robotike za početnike?",
            "Nemam prethodno iskustvo sa senzorima. Da li treba da se pripremim?",
            listOf("radionica", "robotika"), 7
        ),
        question(
            DESIGN_QUESTION, SECOND_HIGHSCHOOL_STUDENT, MOST, "Šta da stavim u portfolio?",
            "Imam radove iz škole i nekoliko samostalnih plakata. Kako da ih izaberem?",
            listOf("prijemni", "dizajn"), 8
        ),
        question(
            FAIR_QUESTION, HIGHSCHOOL_STUDENT, null, "Koja pitanja da pripremim za sajam?",
            "Želim da uporedim programe i saznam koliko imaju praktične nastave.",
            listOf("sajam", "studiranje"), 10
        ),
        question(
            MEDIA_QUESTION, SECOND_HIGHSCHOOL_STUDENT, ISKRA, "Da li treba poneti opremu u studio?",
            "Nemam svoj mikrofon. Da li je oprema dostupna tokom obilaska?",
            listOf("mediji", "otvorena vrata"), 12
        ),
        question(
            HIDDEN_QUESTION, HIGHSCHOOL_STUDENT, null, "Sakrivena demo tema",
            "Primer teme koja se ne pojavljuje u javnom prikazu.",
            emptyList(), 13, status = ForumContentStatus.HIDDEN
        )
    )

    val answers = listOf(
        answer(
            VISIT_STUDENT_ANSWER, VISIT_QUESTION, STUDENT,
            "Pogledaj studentske projekte u laboratoriji. Posle obilaska ima vremena za pitanja.", 6
        ),
        answer(
            VISIT_PROFESSOR_ANSWER, VISIT_QUESTION, VIDIK_PROFESSOR,
            "Obilazak traje oko sat vremena i obuhvata dve praktične demonstracije.", 7
        ),
        answer(ROBOTICS_ANSWER, ROBOTICS_QUESTION, VIDIK_PROFESSOR, "Radionica je prilagođena početnicima.", 8),
        answer("demo-answer-robotics-student", ROBOTICS_QUESTION, STUDENT, "Materijal i primer koda dobijaš na licu mesta.", 9),
        answer(DESIGN_ANSWER, DESIGN_QUESTION, MOST_PROFESSOR, "Izaberi raznovrsne radove i prikaži kako su nastali.", 10),
        answer(FAIR_ANSWER, FAIR_QUESTION, STUDENT, "Pitaj koliko ima laboratorijskih vežbi i kako izgleda jedan projekat.", 11),
        answer(MEDIA_ANSWER, MEDIA_QUESTION, ISKRA_PROFESSOR, "Oprema je dostupna. Potrebno je samo da se prijaviš.", 13),
        answer("demo-answer-hidden", HIDDEN_QUESTION, STUDENT, "Odgovor na sakrivenu demo temu.", 14)
    )

    val enrollments = listOf(
        Enrollment(
            pairId(ROBOTICS, HIGHSCHOOL_STUDENT), ROBOTICS, HIGHSCHOOL_STUDENT,
            EnrollmentStatus.ACTIVE, day(22), day(22)
        ),
        Enrollment(
            pairId(DESIGN, SECOND_HIGHSCHOOL_STUDENT), DESIGN, SECOND_HIGHSCHOOL_STUDENT,
            EnrollmentStatus.CANCELLED, day(22), day(25), cancellationReason = "Termin mi ne odgovara."
        ),
        Enrollment(
            pairId(OPEN_DAY, STUDENT), OPEN_DAY, STUDENT, EnrollmentStatus.COMPLETED,
            day(16), day(22), completionNote = "Obišao sam obe laboratorije."
        )
    )

    val votes = listOf(
        vote(VISIT_STUDENT_ANSWER, HIGHSCHOOL_STUDENT, 8),
        vote(VISIT_STUDENT_ANSWER, ADMIN, 8),
        vote(VISIT_PROFESSOR_ANSWER, STUDENT, 8),
        vote(DESIGN_ANSWER, VIDIK_PROFESSOR, 11),
        vote(MEDIA_ANSWER, SECOND_HIGHSCHOOL_STUDENT, 14)
    )

    val publishedActivities = activities.filter { it.status == ActivityStatus.PUBLISHED }
    val publishedQuestions = questions.filter { it.status == ForumContentStatus.PUBLISHED }
    val visibleAnswers = answers.filter { answer ->
        answer.status == ForumContentStatus.PUBLISHED && publishedQuestions.any { it.id == answer.questionId }
    }

    private fun day(offset: Long): Instant = epoch.plus(offset, ChronoUnit.DAYS)

    private fun user(
        id: String,
        name: String,
        role: UserRole,
        schoolId: String? = null,
        programId: String? = null
    ) = UserProfile(id, name, "$id@example.invalid", role, AccountStatus.ACTIVE, epoch, schoolId, programId)

    private fun activity(
        id: String,
        schoolId: String,
        authorId: String,
        title: String,
        description: String,
        type: ActivityType,
        eventDay: Long,
        location: String,
        capacity: Int,
        status: ActivityStatus = ActivityStatus.PUBLISHED,
        visitCount: Int? = null
    ) = PromoActivity(
        id, schoolId, authorId, title, description, type, day(eventDay), day(eventDay).plusSeconds(7200),
        location, capacity, 0L, status, day(1), if (status == ActivityStatus.CANCELLED) day(20) else day(1),
        visitCount
    )

    private fun question(
        id: String,
        authorId: String,
        schoolId: String?,
        title: String,
        body: String,
        tags: List<String>,
        createdDay: Long,
        acceptedAnswerId: String? = null,
        status: ForumContentStatus = ForumContentStatus.PUBLISHED
    ) = Question(
        id, authorId, title, body, status, day(createdDay),
        day(createdDay + if (acceptedAnswerId != null || status == ForumContentStatus.HIDDEN) 2 else 0),
        schoolId, tags, acceptedAnswerId
    )

    private fun answer(id: String, questionId: String, authorId: String, body: String, createdDay: Long) =
        Answer(id, questionId, authorId, body, ForumContentStatus.PUBLISHED, day(createdDay), day(createdDay))

    private fun vote(answerId: String, userId: String, createdDay: Long) =
        AnswerVote(pairId(answerId, userId), answerId, userId, day(createdDay))

    private fun pairId(parentId: String, userId: String): String {
        val encoder = Base64.getUrlEncoder().withoutPadding()
        val parent = encoder.encodeToString(parentId.toByteArray(Charsets.UTF_8))
        val user = encoder.encodeToString(userId.toByteArray(Charsets.UTF_8))
        return "v1.$parent.$user"
    }
}
