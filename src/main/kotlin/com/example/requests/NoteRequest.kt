package com.example.requests

import com.example.models.Note
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class NoteRequest(
    val title: String,
    val message: String
)

fun NoteRequest.toNote(
    id: UUID = UUID.randomUUID()
): Note {
    return Note(
        id = id,
        title= title,
        message = message
    )
}