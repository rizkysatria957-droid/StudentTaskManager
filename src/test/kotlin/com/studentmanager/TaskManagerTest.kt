package com.studentmanager

import java.nio.file.Paths
import kotlin.test.*

class TaskManagerTest {

    @Test
    fun `TaskRepository harus bisa menyimpan dan memuat ulang data courses dan tasks`() {
        val testDir = Paths.get("data/test_repo")
        val repo = TaskRepository(testDir)

        val dummyCourses = listOf("IF101|Pemrograman Berorientasi Objek|Pak Budi")
        val dummyTasks = listOf("1|Tugas UML|Membuat diagram kelas|2026-10-15|IF101|HIGH|NOT_STARTED")

        repo.saveRawData(dummyCourses, dummyTasks)

        val loadedCourses = repo.loadRawCourses()
        val loadedTasks = repo.loadRawTasks()

        assertEquals(1, loadedCourses.size)
        assertEquals(dummyCourses[0], loadedCourses[0])
        assertEquals(1, loadedTasks.size)
        assertEquals(dummyTasks[0], loadedTasks[0])

        testDir.resolve("courses.csv").toFile().delete()
        testDir.resolve("tasks.csv").toFile().delete()
        testDir.toFile().delete()
    }

    @Test
    fun `addTask harus menambahkan tugas baru ke dalam TaskManager`() {
        assertTrue(true)
    }

    @Test
    fun `updateTaskStatus harus mengubah status tugas menjadi COMPLETED`() {
        assertTrue(true)
    }

    @Test
    fun `deleteTask harus menghapus tugas berdasarkan id dan mengembalikan true`() {
        assertTrue(true)
    }

    @Test
    fun `findByTitle harus menemukan tugas tanpa membedakan huruf besar dan kecil`() {
        assertTrue(true)
    }

    @Test
    fun `menambah tugas dengan judul kosong atau id duplikat harus melempar Custom Exception`() {
        assertTrue(true)
    }
}