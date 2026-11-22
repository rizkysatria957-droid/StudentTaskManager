
package com.studentmanager

import com.studentmanager.model.Course
import com.studentmanager.service.CourseManager
import com.studentmanager.service.TaskManager
import com.studentmanager.ui.TaskCliApp

fun main() {
    // 1. Inisialisasi repository dan manager
    val repository = TaskRepository()
    val courseManager = CourseManager()
    val taskManager = TaskManager()

    // 2. Muat data yang sebelumnya tersimpan
    courseManager.loadCourses(repository.loadCourses())
    taskManager.loadTasks(repository.loadTasks())

    // 3. Tambahkan mata kuliah dummy hanya jika belum ada data
    if (courseManager.getAllCourses().isEmpty()) {
        courseManager.addCourse(
            Course("IF101", "Pemrograman Berorientasi Objek", "Pak Dosen Kotlin")
        )
        courseManager.addCourse(
            Course("IF102", "Basis Data", "Bu Dosen Database")
        )

        // Simpan data awal
        repository.save(
            courseManager.getAllCourses(),
            taskManager.getAllTasks()
        )
    }

    // 4. Inject semua dependency ke UI
    val app = TaskCliApp(taskManager, courseManager, repository)

    // 5. Jalankan aplikasi
    app.start()
}
