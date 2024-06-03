package com.example.routes


import com.example.requests.NoteRequest
import com.example.requests.toNote
import com.example.services.NoteService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureNoteRouting(service: NoteService) {
    routing {



        get("/notes") {
            val response = service.findAll().map { it.toNoteResponse() }
            call.respond(HttpStatusCode.OK, response)
        }
        get("/notes/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            service.findById(id)?.let {
                note ->
                val response = note.toNoteResponse()
                call.respond(response)
            } ?: call.respond(HttpStatusCode.NotFound)
        }
        post("/notes") {
            val note = call.receive<NoteRequest>().toNote()
            val response = service.save(note).toNoteResponse()
            call.respond(HttpStatusCode.Created, response)
        }

        delete("/notes/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            service.delete(id)
            call.respond(HttpStatusCode.OK)
        }

        put("/notes/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val updatedNote = call.receive<NoteRequest>().toNote()
            val response = service.update(id,updatedNote).toNoteResponse()
            call.respond(HttpStatusCode.OK, response)
        }

    }
}