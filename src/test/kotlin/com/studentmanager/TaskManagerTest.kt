package com.studentmanager

import com.studentmanager.model.Course
import com.studentmanager.model.Priority
import com.studentmanager.model.Task
import com.studentmanager.model.TaskStatus
import com.studentmanager.service.CourseManager
import com.studentmanager.service.TaskManager
import java.nio.file.Paths
import java.time.LocalDate
import kotlin.test.*

class TaskManagerTest {

    private lateinit var taskManager: TaskManager
    private lateinit var courseManager: CourseManager

    @BeforeTest
    fun setUp() {
        taskManager = TaskManager()
        courseManager = CourseManager()

        courseManager.addCourse(Course("IF101", "Pemrograman Berorientasi Objek", "Pak Budi"))
    }

    @Test
    fun `addTask harus berhasil menambahkan tugas valid ke dalam daftar`() {
        val task = Task(
            id = 1,
            title = "Membuat Class Diagram",
            description = "Menggambar UML dengan draw.io",
            deadline = LocalDate.now().plusDays(3),
            courseId = "IF101",
            priority = Priority.HIGH
        )

        val hasil = taskManager.addTask(task)

        assertTrue(hasil)
        assertEquals(1, taskManager.getAllTasks().size)
        assertEquals(TaskStatus.NOT_STARTED, taskManager.findTask(1)?.status)
    }

    @Test
    fun `addTask harus menolak tugas dengan ID duplikat`() {
        val task1 = Task(1, "Tugas 1", "Deskripsi 1", LocalDate.now().plusDays(2), "IF101", Priority.LOW)
        val taskDuplikat = Task(1, "Tugas 2", "Deskripsi 2", LocalDate.now().plusDays(4), "IF101", Priority.MEDIUM)

        assertTrue(taskManager.addTask(task1))
        assertFalse(taskManager.addTask(taskDuplikat))
        assertEquals(1, taskManager.getAllTasks().size)
    }

    @Test
    fun `addTask harus menolak tugas dengan judul kosong atau deadline masa lalu`() {
        val taskJudulKosong = Task(2, "   ", "Deskripsi", LocalDate.now().plusDays(2), "IF101", Priority.LOW)
        val taskDeadlineLewat = Task(3, "Tugas Telat", "Deskripsi", LocalDate.now().minusDays(1), "IF101", Priority.HIGH)

        assertFalse(taskManager.addTask(taskJudulKosong))
        assertFalse(taskManager.addTask(taskDeadlineLewat))
        assertTrue(taskManager.getAllTasks().isEmpty())
    }

    @Test
    fun `updateTaskStatus harus mengubah status tugas menjadi COMPLETED`() {
        val task = Task(1, "Laporan Praktikum", "Bab 1-6", LocalDate.now().plusDays(5), "IF101", Priority.MEDIUM)
        taskManager.addTask(task)

        val berhasil = taskManager.updateTaskStatus(1, TaskStatus.COMPLETED)

        assertTrue(berhasil)
        assertEquals(TaskStatus.COMPLETED, taskManager.findTask(1)?.status)
    }

    @Test
    fun `deleteTask harus menghapus tugas dan mengembalikan false jika ID tidak ada`() {
        val task = Task(1, "Kuis Kotlin", "Persiapan UTS", LocalDate.now().plusDays(1), "IF101", Priority.HIGH)
        taskManager.addTask(task)

        assertTrue(taskManager.deleteTask(1))
        assertNull(taskManager.findTask(1))
        assertFalse(taskManager.deleteTask(999))
    }

    @Test
    fun `findByTitle harus mencari judul tanpa membedakan huruf besar dan kecil`() {
        taskManager.addTask(Task(1, "Proyek Mandiri Kotlin", "CLI App", LocalDate.now().plusDays(3), "IF101", Priority.HIGH))
        taskManager.addTask(Task(2, "Tugas Basis Data", "ERD", LocalDate.now().plusDays(4), "IF101", Priority.LOW))

        val hasilCari = taskManager.findByTitle("kotlin")

        assertEquals(1, hasilCari.size)
        assertEquals("Proyek Mandiri Kotlin", hasilCari.first().title)
    }

    @Test
    fun `getTasksSortedByDeadline harus mengurutkan tugas dari deadline terdekat`() {
        val tugasJauh = Task(1, "Tugas Akhir", "Final", LocalDate.now().plusDays(10), "IF101", Priority.LOW)
        val tugasDekat = Task(2, "Tugas Harian", "Cepat", LocalDate.now().plusDays(1), "IF101", Priority.HIGH)

        taskManager.addTask(tugasJauh)
        taskManager.addTask(tugasDekat)

        val terurut = taskManager.getTasksSortedByDeadline()

        assertEquals(2, terurut[0].id)
        assertEquals(1, terurut[1].id)
    }

    @Test
    fun `TaskRepository harus menyimpan dan memuat ulang object Course dan Task dengan akurat`() {
        val testDir = Paths.get("data/test_integration")
        val repo = TaskRepository(testDir)

        val courses = listOf(Course("IF101", "PBO Kotlin", "Pak Budi"))
        val tasks = listOf(
            Task(1, "Implementasi Repo", "Simpan ke CSV", LocalDate.now().plusDays(2), "IF101", Priority.HIGH, TaskStatus.IN_PROGRESS)
        )

        repo.save(courses, tasks)

        val loadedCourses = repo.loadCourses()
        val loadedTasks = repo.loadTasks()

        assertEquals(1, loadedCourses.size)
        assertEquals("PBO Kotlin", loadedCourses[0].name)

        assertEquals(1, loadedTasks.size)
        assertEquals("Implementasi Repo", loadedTasks[0].title)
        assertEquals(Priority.HIGH, loadedTasks[0].priority)
        assertEquals(TaskStatus.IN_PROGRESS, loadedTasks[0].status)

        // Bersihkan file bekas testing
        testDir.resolve("courses.csv").toFile().delete()
        testDir.resolve("tasks.csv").toFile().delete()
        testDir.toFile().delete()
    }
}