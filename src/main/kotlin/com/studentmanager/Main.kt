package com.studentmanager
import com.studentmanager.model.Course
import com.studentmanager.service.CourseManager
import com.studentmanager.service.TaskManager
import com.studentmanager.ui.TaskCliApp

fun main() {
    // 1. Inisialisasi Service / Dapur Logika
    val courseManager = CourseManager()
    val taskManager = TaskManager()

    // 2. Data Dummy Awal (Opsional, biar pas aplikasi baru dibuka gak langsung kosong melompong)
    courseManager.addCourse(Course("IF101", "Pemrograman Berorientasi Objek", "Pak Dosen Kotlin"))
    courseManager.addCourse(Course("IF102", "Basis Data", "Bu Dosen Database"))

    // 3. Panggil UI Developer (Inject dependency Service ke Constructor UI)
    val app = TaskCliApp(taskManager, courseManager)

    // 4. Jalankan Menu Terminal
    app.start()
}