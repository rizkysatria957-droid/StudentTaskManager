package com.studentmanager.model

/** Data pengguna dengan peran mahasiswa. */
class Student(
    name: String,
    email: String,
    val nim: String
) : User(name, email) {

    override fun displayRole(): String = "Mahasiswa"
}
