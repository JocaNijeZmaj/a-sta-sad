package rs.astasad.app.data.model

enum class UserRole {
    ADMIN,
    PROFESSOR,
    STUDENT,
    HIGHSCHOOL_STUDENT
}

enum class AccountStatus {
    ACTIVE,
    BANNED
}

enum class ActivityType {
    FAIR,
    SCHOOL_TOUR,
    WORKSHOP
}

enum class ActivityStatus {
    PUBLISHED,
    CANCELLED,
    REMOVED
}

enum class EnrollmentStatus {
    ACTIVE,
    CANCELLED,
    COMPLETED
}

enum class ForumContentStatus {
    PUBLISHED,
    HIDDEN
}

enum class ReportStatus {
    OPEN,
    RESOLVED,
    DISMISSED
}

enum class ReportTargetType {
    QUESTION,
    ANSWER
}
