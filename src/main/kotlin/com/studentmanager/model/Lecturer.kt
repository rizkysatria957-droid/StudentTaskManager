package com.studentmanager.model

class Lecturer(
    name: String,
    email: String,
    val lecturerId: String
) : User(name, email) {

    override fun displayRole(): String = "Dosen"
}
