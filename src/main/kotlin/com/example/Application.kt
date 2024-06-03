package com.example

import com.example.plugins.*
import com.example.routes.configureBugRouting
import com.example.routes.configureNoteRouting
import com.example.services.BugService
import com.example.services.NoteService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import io.ktor.server.plugins.contentnegotiation.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

//fun Application.module() {
//    configureSerialization()
//    configureDatabases()
//    configureRouting()
//
//}
fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    val database = Database.connect(
        url = "jdbc:h2:file:./database/db;MODE=MYSQL;DB_CLOSE_DELAY=-1;",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val serviceNotes = NoteService(database)
    val bugService = BugService(database)
    configureNoteRouting(serviceNotes)
    configureBugRouting(bugService)
}