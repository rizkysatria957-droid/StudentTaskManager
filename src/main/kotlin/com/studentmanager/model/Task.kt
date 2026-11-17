package com.studentmanager.model

import java.time.LocalDate

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

    fun updateStatus(newStatus: TaskStatus) {
        status = newStatus
    }

    fun displayTask() {
        println(
            """
            ID: $id
            Judul: $title
            Deskripsi: $description
            Deadline: $deadline
            Mata Kuliah ID: $courseId
            Prioritas: $priority
            Status: $status
            """.trimIndent()
        )
    }
}