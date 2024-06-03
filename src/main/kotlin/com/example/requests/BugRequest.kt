package com.example.requests

import com.example.models.BugReport
import com.example.models.createBugReport
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class BugRequest (
    val title: String,
    val description: String,
    val severity: String,
    val status: String )

fun BugRequest.toBug(
    id: UUID = UUID.randomUUID()
): BugReport {
    return createBugReport(
        id = id,
        title = title,
        description = description,
        severity = severity,
        status = status
    )
}
