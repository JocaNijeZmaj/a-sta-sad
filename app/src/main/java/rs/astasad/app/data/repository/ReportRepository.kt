package rs.astasad.app.data.repository

import rs.astasad.app.data.model.ContentReport
import rs.astasad.app.data.model.ReportTargetType

/** Self-service operations resolve the reporter from the authenticated session. */
interface ReportRepository {
    suspend fun submitReport(targetType: ReportTargetType, targetId: String, reason: String): ContentReport
    suspend fun listOwnReports(): List<ContentReport>

    /** The moderation queue and its decisions require administrator authorization. */
    suspend fun listOpenReports(): List<ContentReport>
    suspend fun resolveReport(reportId: String)
    suspend fun dismissReport(reportId: String)
}
