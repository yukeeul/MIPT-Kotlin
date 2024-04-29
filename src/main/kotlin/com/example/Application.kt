package com.example

import com.example.plugins.configureAuth
import com.example.plugins.configureDatabase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, module=Application::module).start(wait = true)
}
fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureAuth()
    configureRouting()
}


