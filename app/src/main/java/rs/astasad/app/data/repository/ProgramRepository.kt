package rs.astasad.app.data.repository

import rs.astasad.app.data.model.Program

/** Catalog writes require administrator authorization. A program keeps its school when edited. */
interface ProgramRepository {
    suspend fun listPrograms(schoolId: String): List<Program>
    suspend fun getProgram(programId: String): Program?
    suspend fun createProgram(schoolId: String, title: String, description: String): Program
    suspend fun updateProgram(programId: String, title: String, description: String)
    suspend fun deleteProgram(programId: String)
}
