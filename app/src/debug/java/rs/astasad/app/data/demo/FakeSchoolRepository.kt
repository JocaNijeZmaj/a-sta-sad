package rs.astasad.app.data.demo

import rs.astasad.app.data.model.School
import rs.astasad.app.data.repository.SchoolDetails
import rs.astasad.app.data.repository.SchoolRepository

internal class FakeSchoolRepository : SchoolRepository {
    override suspend fun listSchools(): List<School> =
        DemoData.schools.sortedWith(compareBy(School::name, School::id)).take(DEMO_PAGE_SIZE)

    override suspend fun getSchool(schoolId: String): School? =
        DemoData.schools.firstOrNull { it.id == schoolId }

    override suspend fun createSchool(details: SchoolDetails): School = unsupportedDemoOperation()
    override suspend fun updateSchool(schoolId: String, details: SchoolDetails): Unit = unsupportedDemoOperation()
    override suspend fun deleteSchool(schoolId: String): Unit = unsupportedDemoOperation()
}
