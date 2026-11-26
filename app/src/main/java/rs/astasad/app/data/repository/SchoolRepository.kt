package rs.astasad.app.data.repository

import rs.astasad.app.data.model.School

/** Catalog writes require administrator authorization. */
interface SchoolRepository {
    suspend fun listSchools(): List<School>
    suspend fun getSchool(schoolId: String): School?
    suspend fun createSchool(details: SchoolDetails): School
    suspend fun updateSchool(schoolId: String, details: SchoolDetails)
    suspend fun deleteSchool(schoolId: String)
}

data class SchoolDetails(
    val name: String,
    val city: String,
    val description: String,
    val website: String? = null
)
