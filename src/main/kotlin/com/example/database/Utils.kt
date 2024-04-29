package com.example.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

fun getConnection(): Database{
    return Database.connect(
        url = "jdbc:postgresql://localhost:9000/practice_db",
        user = "mipt_kotlin_backend",
        driver = "org.postgresql.Driver",
        password = "simple_password"
    )
}

fun hasConnection(): Boolean {
    getConnection()
    return transaction {
        try {
            !connection.isClosed
        } catch (e: Exception) {
            false
        }
    }
}