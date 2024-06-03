package com.example.routes

import com.example.models.BugReport
import com.example.requests.BugRequest
import com.example.requests.toBug
import com.example.services.BugService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

fun Application.configureBugRouting(service: BugService) {
    routing {

        get("/bugs") {
            val response = service.findAll().map { it.toBugResponse() }
            call.respond(HttpStatusCode.OK, response)
        }

        get("/bugs/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            service.findById(id)?.let {
                bug ->
                val response = bug.toBugResponse()
                call.respond(response)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        post("/bugs") {
            val bug = call.receive<BugRequest>().toBug()
            val response = service.save(bug).toBugResponse()
            call.respond(HttpStatusCode.Created, response)
        }

        delete("/bugs/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            service.delete(id)
            call.respond(HttpStatusCode.OK)
        }
        put("/bugs/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val updatedBug = call.receive<BugRequest>().toBug(id)
            val response = service.update(id, updatedBug).toBugResponse()
            call.respond(HttpStatusCode.OK, response)
        }

        post("/bugs/upload/{filePath...}") {
            val filePath = call.parameters.getAll("filePath")?.joinToString("/") ?: return@post call.respond(HttpStatusCode.BadRequest)
            var bugList = listOf<BugReport>()
            try {
                bugList = service.uploadBugs(filePath)
            } catch (e: ExposedSQLException) {
                if (e.message?.contains("primary key violation") == true) {
                    call.respond(HttpStatusCode.Conflict)
                }
                call.respond(HttpStatusCode.InternalServerError)
            }

            val response = bugList.map{
                it.toBugResponse()
            }
            call.respond(response)
        }
    }
}