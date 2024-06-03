package com.example.services

import com.example.models.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.UUID

class BugService(database: Database) {
    private object Bugreports : Table() {
        val id = uuid("id")
        val title = varchar("title", 255)
        val description = text("description")
        val severity = varchar("severity", 255)
        val status = varchar("status", 255)

        override val primaryKey = PrimaryKey(id, name= "PK_Bug_ID")
    }

    init {
        transaction(database) { SchemaUtils.create(Bugreports) }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


    suspend fun findAll(): List<BugReport> = dbQuery {
        Bugreports.selectAll().map { row ->
            row.toBug()
        }
    }

    private fun ResultRow.toBug() =
        createBugReport(
            id = this[Bugreports.id],
            title = this[Bugreports.title],
            description = this[Bugreports.description],
            severity = this[Bugreports.severity],
            status = this[Bugreports.status]
        )

    suspend fun findById(id: UUID): BugReport? = dbQuery {Bugreports.select {Bugreports.id eq id}.map {
        row -> row.toBug()
    }.singleOrNull()}

    suspend fun save(bug: BugReport): BugReport = dbQuery {
        Bugreports.insert {
            it[id] = bug.id
            it[title] = bug.title
            it[description] = bug.description
            it[severity] = bug.severity
            it[status] = bug.status
        }    .let {
            createBugReport(id = it[Bugreports.id],
                title = it[Bugreports.title],
                description = it[Bugreports.description],
                severity = it[Bugreports.severity],
                status = it[Bugreports.status])
        }
    }

    suspend fun delete(id: UUID) = dbQuery {
        Bugreports.deleteWhere { Bugreports.id eq id }
    }

    suspend fun update(id: UUID, updated: BugReport): BugReport = dbQuery {
        Bugreports.update({Bugreports.id eq id}) {
            it[title] = updated.title
            it[description] = updated.description
            it[severity] = updated.severity
            it[status] = updated.status
        }
        return@dbQuery updated
    }


    suspend fun uploadBugs(filePath: String): List<BugReport> {
        val file = File(filePath)
        val data = file.readLines()
        val bugList = mutableListOf<BugReport>()
        data.forEach {
            val (id,title,description,severity,status) = it.split(",")
            val bug = createBugReport(id = UUID.fromString(id),title=title, description = description, severity = severity, status = status)

            bugList.add(save(bug))
        }
        return bugList
    }

}