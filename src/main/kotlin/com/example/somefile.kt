package com.example

import io.ktor.util.Identity.decode
import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        println("Running on thread: ${Thread.currentThread().name}")
        delay(3000L)
        println("Finished on thread: ${Thread.currentThread().name}")
    }

    job.join()
    val otherJob = launch(Dispatchers.Default) {
        println("This is some other job on thread: ${Thread.currentThread().name}")
    }
    otherJob.join()
    println("Completed")
}