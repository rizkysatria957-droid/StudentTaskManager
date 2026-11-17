package com.studentmanager.model

/** Data pengguna dengan peran dosen. */
class Lecturer(
    name: String,
    email: String,
    val lecturerId: String
) : User(name, email) {

    override fun displayRole(): String = "Dosen"
}
