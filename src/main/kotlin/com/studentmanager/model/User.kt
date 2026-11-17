package com.studentmanager.model

/**
 * Kelas induk untuk pengguna Student Task Manager.
 * User bersifat abstract sehingga objek dibuat melalui Student atau Lecturer.
 */
abstract class User(
    val name: String,
    val email: String
) {
    /** Mengembalikan label peran pengguna. */
    abstract fun displayRole(): String
}
