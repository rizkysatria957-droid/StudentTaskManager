package com.studentmanager.model

class Student(
    name: String,
    email: String,
    val nim: String
) : User(name, email) {

    override fun displayRole(): String = "Mahasiswa"
}
