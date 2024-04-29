package com.example.database

import org.jetbrains.exposed.sql.Table

class DatabaseSchemas{
    object Users : Table() {
        val login = varchar("login", length = 50)
        val password = varchar("password", length = 50)
        val name = varchar("name", length = 50)
        val creationDate = varchar("creationDate", length = 50)

        override val primaryKey = PrimaryKey(login)
    }

    object Articles : Table() {
        val id = integer("id").autoIncrement()
        val contents = varchar("contents", length = 501)
        val creationDate = varchar("creationDate", length = 50)
        val changeDate = varchar("changeDate", length = 50)
        val author = varchar("author", length = 501)

        override val primaryKey = PrimaryKey(id)
    }

}