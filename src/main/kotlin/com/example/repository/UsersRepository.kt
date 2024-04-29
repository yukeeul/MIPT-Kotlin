package com.example.repository

import com.example.model.User

interface UsersRepository {
    fun getByLogin(login: String): User?

    fun hasLogin(login: String): Boolean

    fun createUser(login: String, password: String, name: String): User?
}