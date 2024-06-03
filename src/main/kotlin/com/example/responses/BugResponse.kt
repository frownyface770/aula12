package com.example.responses

import java.util.UUID
import kotlinx.serialization.Serializable
@Serializable
class BugResponse (
    val id: String,
    val title: String,
    val description: String,
    val severity: String,
    val status: String )

