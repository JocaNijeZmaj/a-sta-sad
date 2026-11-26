package rs.astasad.app.data.model

import java.time.Instant

data class ContentReport(
    val id: String,
    val reporterId: String,
    val targetType: ReportTargetType,
    val targetId: String,
    val reason: String,
    val status: ReportStatus,
    val createdAt: Instant
)
