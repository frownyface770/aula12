package com.example.models

import com.example.responses.NoteResponse
import java.util.UUID


class Note(val id: UUID = UUID.randomUUID(), val title: String, val message: String) {

    fun toNoteResponse(): NoteResponse {

        return NoteResponse(id = id.toString(), title = title, message = message)
    }
}