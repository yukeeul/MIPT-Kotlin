package com.example.plugins

import com.example.database.DatabaseSchemas
import com.example.database.getConnection
import com.example.database.hasConnection
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    if (hasConnection()){
        val database: Database = getConnection()
        transaction(database) {
            SchemaUtils.create(DatabaseSchemas.Users)
        }

        transaction(database) {
            SchemaUtils.create(DatabaseSchemas.Articles)
        }
    }



}