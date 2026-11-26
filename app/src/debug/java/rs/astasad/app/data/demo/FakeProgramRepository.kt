package rs.astasad.app.data.demo

import rs.astasad.app.data.model.Program
import rs.astasad.app.data.repository.ProgramRepository

internal class FakeProgramRepository : ProgramRepository {
    override suspend fun listPrograms(schoolId: String): List<Program> =
        DemoData.programs.filter { it.schoolId == schoolId }
            .sortedWith(compareBy(Program::title, Program::id))
            .take(DEMO_PAGE_SIZE)

    override suspend fun getProgram(programId: String): Program? =
        DemoData.programs.firstOrNull { it.id == programId }

    override suspend fun createProgram(schoolId: String, title: String, description: String): Program =
        unsupportedDemoOperation()

    override suspend fun updateProgram(programId: String, title: String, description: String): Unit =
        unsupportedDemoOperation()

    override suspend fun deleteProgram(programId: String): Unit = unsupportedDemoOperation()
}
