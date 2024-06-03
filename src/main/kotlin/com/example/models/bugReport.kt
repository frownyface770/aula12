package com.example.models

import com.example.responses.BugResponse
import java.util.UUID

open class BugReport(val id: UUID, val title: String, val description: String, val severity:String, var status: String) {

    fun toBugResponse():BugResponse {
        return BugResponse(id= id.toString(),title=title, description=description,severity=severity,status=status)
    }
}
fun createBugReport(id: UUID = UUID.randomUUID(), title: String, description: String, severity: String, status: String): BugReport {
    println("$title and $severity")
    return when (severity) {
        "Critical" -> CriticalBug(id, title, description, status)
        "Major" -> MajorBug(id, title, description, status)
        "Minor" -> MinorBug(id, title, description, status)
        else -> throw IllegalArgumentException("Unknown severity: $severity")
    }
}

class CriticalBug(id: UUID,  title: String, description: String, status: String): BugReport(id,title,description,"Critical" ,status) {

}
class MajorBug(id:UUID, title: String, description: String, status: String): BugReport(id, title,description,"Major", status) {

}
class MinorBug(id: UUID, title:String, description: String, status: String): BugReport(id, title,description,"Minor", status) {

}
