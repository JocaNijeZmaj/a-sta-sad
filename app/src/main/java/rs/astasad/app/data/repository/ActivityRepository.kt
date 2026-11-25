package rs.astasad.app.data.repository

import java.time.Instant
import rs.astasad.app.data.model.ActivityStatus
import rs.astasad.app.data.model.ActivityType
import rs.astasad.app.data.model.PromoActivity

/** Activity writes require administrator or school-scoped professor authorization. */
interface ActivityRepository {
    /** Returns only activities visible to the caller, even when a status filter is supplied. */
    suspend fun listActivities(filter: ActivityFilter = ActivityFilter()): List<PromoActivity>
    suspend fun getActivity(activityId: String): PromoActivity?

    /** The backend resolves the author and a professor's school. Only admins can select a school. */
    suspend fun createActivity(details: ActivityDetails, targetSchoolId: String? = null): PromoActivity

    suspend fun updateActivity(activityId: String, details: ActivityDetails)
    suspend fun cancelActivity(activityId: String)
    suspend fun removeActivity(activityId: String)
}

data class ActivityFilter(
    val search: String? = null,
    val schoolId: String? = null,
    val type: ActivityType? = null,
    val startsFrom: Instant? = null,
    val startsBefore: Instant? = null,
    val status: ActivityStatus? = null
)

data class ActivityDetails(
    val title: String,
    val description: String,
    val type: ActivityType,
    val startsAt: Instant,
    val endsAt: Instant,
    val location: String,
    val capacity: Int,
    val costMinorUnits: Long
)
