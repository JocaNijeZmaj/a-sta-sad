package rs.astasad.app.data.demo

import rs.astasad.app.data.model.PromoActivity
import rs.astasad.app.data.repository.ActivityDetails
import rs.astasad.app.data.repository.ActivityFilter
import rs.astasad.app.data.repository.ActivityRepository

internal class FakeActivityRepository : ActivityRepository {
    override suspend fun listActivities(filter: ActivityFilter): List<PromoActivity> {
        val loaded = DemoData.publishedActivities
            .filter { activity ->
                (filter.schoolId == null || filter.schoolId == activity.schoolId) &&
                    (filter.type == null || filter.type == activity.type) &&
                    (filter.status == null || filter.status == activity.status) &&
                    (filter.startsFrom == null || activity.startsAt >= filter.startsFrom) &&
                    (filter.startsBefore == null || activity.startsAt < filter.startsBefore)
            }
            .sortedWith(compareBy(PromoActivity::startsAt, PromoActivity::id))
            .take(DEMO_PAGE_SIZE)

        val search = filter.search.orEmpty().trim()
        return loaded.filter {
            it.title.contains(search, ignoreCase = true) || it.description.contains(search, ignoreCase = true)
        }
    }

    override suspend fun getActivity(activityId: String): PromoActivity? =
        DemoData.publishedActivities.firstOrNull { it.id == activityId }

    override suspend fun createActivity(details: ActivityDetails, targetSchoolId: String?): PromoActivity =
        unsupportedDemoOperation()

    override suspend fun updateActivity(activityId: String, details: ActivityDetails): Unit =
        unsupportedDemoOperation()

    override suspend fun cancelActivity(activityId: String): Unit = unsupportedDemoOperation()
    override suspend fun removeActivity(activityId: String): Unit = unsupportedDemoOperation()
}
