package com.studentmanager.model

import java.time.LocalDate

/**
 * Model untuk menyimpan informasi tugas mahasiswa.
 *
 * Properti yang dapat diubah dibuat var agar TaskManager dapat menjalankan
 * operasi editTask sesuai rancangan class diagram. Status hanya dapat diubah
 * melalui updateStatus().
 */
class Task(
    val id: Int,
    var title: String,
    var description: String,
    var deadline: LocalDate,
    val courseId: String,
    var priority: Priority,
    status: TaskStatus = TaskStatus.NOT_STARTED
) {
    var status: TaskStatus = status
        private set

    /** Mengubah status tugas. */
    fun updateStatus(newStatus: TaskStatus) {
        status = newStatus
    }

    /**
     * Status dapat dibaca melalui property `status`.
     * Tidak dibuat method getStatus() karena getter property status di Kotlin
     * sudah menghasilkan method JVM getStatus() dan dapat menyebabkan bentrok.
     */

    /** Menampilkan ringkasan tugas ke standard output. */
    fun displayTask() {
        println(
            """ID: $id
Judul: $title
Deskripsi: $description
Deadline: $deadline
Mata Kuliah ID: $courseId
Prioritas: $priority
Status: $status"""
        )
    }
}
