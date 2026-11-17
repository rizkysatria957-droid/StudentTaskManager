package com.studentmanager.model

abstract class User(
    val name: String,
    val email: String
) {
    abstract fun displayRole(): String
}
