package com.studentmanager.ui

import com.studentmanager.model.Course
import com.studentmanager.model.Priority
import com.studentmanager.model.Task
import com.studentmanager.model.TaskStatus
import com.studentmanager.service.CourseManager
import com.studentmanager.service.TaskManager
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * Palet warna terminal menggunakan standar ANSI Escape Code.
 * Berfungsi memanipulasi warna teks, background, dan gaya tulisan di console.
 */
object Ansi {
    const val RESET = "\u001B[0m"
    const val BOLD = "\u001B[1m"
    const val DIM = "\u001B[2m"

    // Foreground / Teks
    const val RED = "\u001B[31m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BLUE = "\u001B[34m"
    const val PURPLE = "\u001B[35m"
    const val CYAN = "\u001B[36m"
    const val WHITE = "\u001B[37m"

    // Background Badges
    const val BG_RED = "\u001B[41m"
    const val BG_GREEN = "\u001B[42m"
    const val BG_YELLOW = "\u001B[43m"
    const val BG_CYAN = "\u001B[46m"
}

class TaskCliApp(
    private val taskManager: TaskManager,
    private val courseManager: CourseManager
) {
    private var nextTaskId = 1

    fun start() {
        var running = true

        while (running) {
            printBanner()
            printMenu()

            when (readInt("${Ansi.BOLD}${Ansi.CYAN}Pilih opsi menu (0-10): ${Ansi.RESET}")) {
                1 -> handleAddTask()
                2 -> handleShowAllTasks()
                3 -> handleUpdateTaskStatus()
                4 -> handleEditTask()
                5 -> handleDeleteTask()
                6 -> handleSearchTask()
                7 -> handleSortTasksByDeadline()
                8 -> handleShowStatisticsWithProgressBar()
                9 -> handleAddCourse()
                10 -> handleShowCourses()
                0 -> {
                    printExitScreen()
                    running = false
                }
                else -> {
                    println("\n${Ansi.RED}[!] Pilihan tidak valid. Silakan pilih nomor yang ada di menu.${Ansi.RESET}")
                    pausePrompt()
                }
            }
        }
    }

    // ==========================================
    // ELEMEN VISUAL & BANNER
    // ==========================================

    private fun printBanner() {
        println(Ansi.CYAN + Ansi.BOLD)
        println("""
        ================================================================
           ____  _             _            _     _____           _     
          / ___|| |_ _   _  __| | ___ _ __ | |_  |_   _|_ _ ___| | __   
          \___ \| __| | | |/ _` |/ _ \ '_ \| __|   | |/ _` / __| |/ /   
           ___) | |_| |_| | (_| |  __/ | | | |_    | | (_| \__ \   <    
          |____/ \__|\__,_|\__,_|\___|_| |_|\__|   |_|\__,_|___/_|\_\   
                      STUDENT TASK MANAGER SYSTEM v1.0                  
        ================================================================
        """.trimIndent() + Ansi.RESET)
    }

    private fun printMenu() {
        println("${Ansi.BOLD}+--- [ MANAJEMEN TUGAS ] ---------------------------------------+${Ansi.RESET}")
        println("|  ${Ansi.CYAN}1.${Ansi.RESET} Tambah Tugas Baru          ${Ansi.CYAN}5.${Ansi.RESET} Hapus Tugas                 |")
        println("|  ${Ansi.CYAN}2.${Ansi.RESET} Lihat Semua Tugas          ${Ansi.CYAN}6.${Ansi.RESET} Cari Tugas (Judul/Deadline) |")
        println("|  ${Ansi.CYAN}3.${Ansi.RESET} Ubah Status Tugas          ${Ansi.CYAN}7.${Ansi.RESET} Urutkan Deadline Terdekat   |")
        println("|  ${Ansi.CYAN}4.${Ansi.RESET} Edit Data Tugas            ${Ansi.CYAN}8.${Ansi.RESET} Statistik & Visual Progress |")
        println("${Ansi.BOLD}+--- [ MANAJEMEN MATA KULIAH ] ---------------------------------+${Ansi.RESET}")
        println("|  ${Ansi.CYAN}9.${Ansi.RESET} Tambah Mata Kuliah        ${Ansi.CYAN}10.${Ansi.RESET} Lihat Daftar Mata Kuliah    |")
        println("${Ansi.BOLD}+--- [ KELUAR ] ------------------------------------------------+${Ansi.RESET}")
        println("|  ${Ansi.RED}0.${Ansi.RESET} Keluar Aplikasi                                           |")
        println("${Ansi.BOLD}+---------------------------------------------------------------+${Ansi.RESET}")
    }
    private fun printExitScreen() {
        println("\n" + Ansi.GREEN + Ansi.BOLD)
        println("================================================================")
        println("       Terima kasih telah menggunakan Student Task Manager!     ")
        println("        Tetap semangat belajarnya & jangan lupa istirahat!      ")
        println("================================================================")
        println(Ansi.RESET)
    }

    // ==========================================
    // HANDLER FITUR TUGAS
    // ==========================================

    private fun handleAddTask() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [1] TAMBAH TUGAS BARU ---${Ansi.RESET}")

        val courses = courseManager.getAllCourses()
        if (courses.isEmpty()) {
            println("${Ansi.YELLOW}[!] Belum ada Mata Kuliah terdaftar.${Ansi.RESET}")
            println("    Daftarkan mata kuliah terlebih dahulu di menu 9.")
            pausePrompt()
            return
        }

        println("${Ansi.DIM}Mata Kuliah Tersedia:${Ansi.RESET}")
        courseManager.showCourses()
        println()

        val courseId = readNonBlankString("Masukkan ID Mata Kuliah: ")
        if (courseManager.findCourse(courseId) == null) {
            println("${Ansi.RED}[!] Error: Mata Kuliah dengan ID '$courseId' tidak ditemukan!${Ansi.RESET}")
            pausePrompt()
            return
        }

        val title = readNonBlankString("Judul Tugas: ")
        val description = readNonBlankString("Deskripsi Singkat: ")
        val deadline = readFutureDate("Deadline Tugas (Format: YYYY-MM-DD): ")
        val priority = readPriority()

        val newTask = Task(
            id = nextTaskId,
            title = title,
            description = description,
            deadline = deadline,
            courseId = courseId,
            priority = priority
        )

        val isSuccess = taskManager.addTask(newTask)
        if (isSuccess) {
            println("\n${Ansi.GREEN}${Ansi.BOLD} [SUKSES] Tugas ID $nextTaskId ('$title') berhasil ditambahkan!${Ansi.RESET}")
            nextTaskId++
        } else {
            println("\n${Ansi.RED}[!] [GAGAL] Tugas gagal disimpan (Deadline tidak boleh di masa lalu).${Ansi.RESET}")
        }
        pausePrompt()
    }

    private fun handleShowAllTasks() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [2] DAFTAR SEMUA TUGAS ---${Ansi.RESET}")
        val tasks = taskManager.getAllTasks()
        if (tasks.isEmpty()) {
            println("${Ansi.YELLOW}Belum ada tugas yang dicatat.${Ansi.RESET}")
        } else {
            tasks.forEach { renderTaskCard(it) }
        }
        pausePrompt()
    }

    private fun handleUpdateTaskStatus() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [3] UBAH STATUS TUGAS ---${Ansi.RESET}")
        val id = readInt("Masukkan ID Tugas yang ingin diubah: ")
        if (id == null) {
            pausePrompt()
            return
        }

        val task = taskManager.findTask(id)
        if (task == null) {
            println("${Ansi.RED}[!] Tugas dengan ID $id tidak ditemukan!${Ansi.RESET}")
            pausePrompt()
            return
        }

        println("Tugas ditemukan: ${Ansi.BOLD}${task.title}${Ansi.RESET}")
        print("Status saat ini: ")
        println(formatStatusBadge(task.status))

        val newStatus = readStatus()
        val isUpdated = taskManager.updateTaskStatus(id, newStatus)

        if (isUpdated) {
            println("\n${Ansi.GREEN} [SUKSES] Status berhasil diubah menjadi: ${formatStatusBadge(newStatus)}${Ansi.RESET}")
        } else {
            println("\n${Ansi.RED}[!] Gagal memperbarui status tugas.${Ansi.RESET}")
        }
        pausePrompt()
    }

    private fun handleEditTask() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [4] EDIT DATA TUGAS ---${Ansi.RESET}")
        val id = readInt("Masukkan ID Tugas yang ingin diedit: ")
        if (id == null) {
            pausePrompt()
            return
        }

        val existingTask = taskManager.findTask(id)
        if (existingTask == null) {
            println("${Ansi.RED}[!] Tugas dengan ID $id tidak ditemukan.${Ansi.RESET}")
            pausePrompt()
            return
        }

        println("${Ansi.DIM}Data saat ini:${Ansi.RESET}")
        renderTaskCard(existingTask)

        println("\n${Ansi.CYAN}Masukkan Data Baru:${Ansi.RESET}")
        val newTitle = readNonBlankString("Judul Baru: ")
        val newDescription = readNonBlankString("Deskripsi Baru: ")
        val newDeadline = readFutureDate("Deadline Baru (YYYY-MM-DD): ")
        val newPriority = readPriority()

        val isSuccess = taskManager.editTask(id, newTitle, newDescription, newDeadline, newPriority)
        if (isSuccess) {
            println("\n${Ansi.GREEN} [SUKSES] Data tugas ID $id berhasil diperbarui!${Ansi.RESET}")
        } else {
            println("\n${Ansi.RED}[!] [GAGAL] Gagal mengedit tugas. Pastikan deadline valid.${Ansi.RESET}")
        }
        pausePrompt()
    }

    private fun handleDeleteTask() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [5] HAPUS TUGAS ---${Ansi.RESET}")
        val id = readInt("Masukkan ID Tugas yang ingin dihapus: ")
        if (id == null) {
            pausePrompt()
            return
        }

        val isDeleted = taskManager.deleteTask(id)
        if (isDeleted) {
            println("\n${Ansi.GREEN} [SUKSES] Tugas ID $id berhasil dihapus dari sistem.${Ansi.RESET}")
        } else {
            println("\n${Ansi.RED}[!] Tugas dengan ID $id tidak ditemukan.${Ansi.RESET}")
        }
        pausePrompt()
    }

    private fun handleSearchTask() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [6] CARI TUGAS ---${Ansi.RESET}")
        println(" 1. Cari berdasarkan Judul")
        println(" 2. Cari berdasarkan Deadline Persis")

        when (readInt("Pilih mode (1-2): ")) {
            1 -> {
                val keyword = readNonBlankString("Masukkan kata kunci judul: ")
                val results = taskManager.findByTitle(keyword)
                displaySearchResults(results)
            }
            2 -> {
                val date = readDate("Masukkan tanggal deadline (YYYY-MM-DD): ")
                val results = taskManager.findByDeadline(date)
                displaySearchResults(results)
            }
            else -> println("${Ansi.RED}[!] Opsi pencarian tidak valid.${Ansi.RESET}")
        }
        pausePrompt()
    }

    private fun handleSortTasksByDeadline() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [7] TUGAS DIURUTKAN BERDASARKAN DEADLINE TERDEKAT ---${Ansi.RESET}")
        val sortedTasks = taskManager.getTasksSortedByDeadline()
        if (sortedTasks.isEmpty()) {
            println("${Ansi.YELLOW}Belum ada tugas.${Ansi.RESET}")
        } else {
            sortedTasks.forEach { renderTaskCard(it) }
        }
        pausePrompt()
    }

    private fun handleShowStatisticsWithProgressBar() {
        // Panggil logic statistik bawaan temen lu
        taskManager.showStatistics()

        // Tambahan polesan visual: Graphical Progress Bar
        val tasks = taskManager.getAllTasks()
        if (tasks.isNotEmpty()) {
            val completed = tasks.count { it.status == TaskStatus.COMPLETED }
            val percentage = (completed * 100.0) / tasks.size
            renderProgressBar(percentage)
        }
        pausePrompt()
    }

    // ==========================================
    // HANDLER FITUR MATA KULIAH
    // ==========================================

    private fun handleAddCourse() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [9] TAMBAH MATA KULIAH BARU ---${Ansi.RESET}")
        val id = readNonBlankString("ID Mata Kuliah (misal: IF101): ")
        val name = readNonBlankString("Nama Mata Kuliah: ")
        val lecturer = readNonBlankString("Nama Dosen Pengampu: ")

        val course = Course(id = id, name = name, lecturerName = lecturer)
        val isSuccess = courseManager.addCourse(course)

        if (isSuccess) {
            println("\n${Ansi.GREEN} [SUKSES] Mata kuliah '${course.name}' berhasil disimpan!${Ansi.RESET}")
        } else {
            println("\n${Ansi.RED}[!] [GAGAL] ID '$id' sudah ada atau nama tidak boleh kosong.${Ansi.RESET}")
        }
        pausePrompt()
    }

    private fun handleShowCourses() {
        println("\n${Ansi.BOLD}${Ansi.PURPLE}--- [10] DAFTAR MATA KULIAH ---${Ansi.RESET}")
        val courses = courseManager.getAllCourses()
        if (courses.isEmpty()) {
            println("${Ansi.YELLOW}Belum ada mata kuliah yang terdaftar.${Ansi.RESET}")
        } else {
            println("+----------+-------------------------------------+-------------------------------+")
            println("| ID KODE  | NAMA MATA KULIAH                    | DOSEN PENGAMPU                |")
            println("+----------+-------------------------------------+-------------------------------+")
            courses.forEach { c ->
                println("| %-8s | %-35s | %-29s |".format(c.id, c.name, c.lecturerName))
            }
            println("+----------+-------------------------------------+-------------------------------+")
        }
        pausePrompt()
    }

    // ==========================================
    // RENDERER TAMPILAN KARTU & VISUAL PROGRESS
    // ==========================================

    private fun renderTaskCard(task: Task) {
        val course = courseManager.findCourse(task.courseId)
        val courseDisplay = course?.let { "${it.name} (${it.id})" } ?: "ID: ${task.courseId}"

        // Total lebar border: 1 + 72 + 1 = 74 karakter
        val border = "+" + "-".repeat(72) + "+"

        // 1. Header (ID + Judul) pas 70 karakter (Total: 2 + 70 + 2 = 74)
        val headerRaw = "#${task.id} ${task.title}"
        val headerFormatted = headerRaw.take(70).padEnd(70)

        // 2. Badge Prioritas (Lebar visual: 10 karakter)
        val (priorityBadge, priorityColor) = when (task.priority) {
            Priority.HIGH -> Pair("[ HIGH ! ]", Ansi.RED + Ansi.BOLD)
            Priority.MEDIUM -> Pair("[ MEDIUM ]", Ansi.YELLOW + Ansi.BOLD)
            Priority.LOW -> Pair("[ LOW    ]", Ansi.CYAN)
        }
        val coloredPriority = "$priorityColor$priorityBadge${Ansi.RESET}"

        // 3. Badge Status (Lebar visual: 15 karakter)
        val (statusBadge, statusColor) = when (task.status) {
            TaskStatus.COMPLETED -> Pair("[ COMPLETED   ]", Ansi.GREEN + Ansi.BOLD)
            TaskStatus.IN_PROGRESS -> Pair("[ IN_PROGRESS ]", Ansi.YELLOW + Ansi.BOLD)
            TaskStatus.NOT_STARTED -> Pair("[ NOT_STARTED ]", Ansi.RED)
        }
        val coloredStatus = "$statusColor$statusBadge${Ansi.RESET}"

        println()
        println(border)
        println("| ${Ansi.BOLD}$headerFormatted${Ansi.RESET} |")
        println(border)
        // Label (15) + Isi (55) + Samping (4) = 74 Karakter Presisi!
        println("| [*] Matkul   : %-55s |".format(courseDisplay.take(55)))
        println("| [*] Deskripsi: %-55s |".format(task.description.take(55)))
        println("| [*] Deadline : %-55s |".format(task.deadline.toString().take(55)))
        println("| [*] Prioritas: $coloredPriority" + " ".repeat(45) + " |")
        println("| [*] Status   : $coloredStatus" + " ".repeat(40) + " |")
        println(border)
    }

    private fun renderProgressBar(percentage: Double) {
        val totalBlocks = 30
        val filledBlocks = ((percentage / 100.0) * totalBlocks).toInt()
        val emptyBlocks = totalBlocks - filledBlocks

        // Pakai karakter '#' dan '-' agar kebal error encoding
        val bar = "#".repeat(filledBlocks) + "-".repeat(emptyBlocks)
        val color = when {
            percentage >= 80.0 -> Ansi.GREEN
            percentage >= 40.0 -> Ansi.YELLOW
            else -> Ansi.RED
        }

        println("\nVisual Progress Bar:")
        println("$color[$bar] %.1f%%${Ansi.RESET}".format(percentage))
    }

    private fun formatPriorityBadge(priority: Priority): String {
        return when (priority) {
            Priority.HIGH -> "${Ansi.RED}${Ansi.BOLD}[ HIGH ! ]${Ansi.RESET}"
            Priority.MEDIUM -> "${Ansi.YELLOW}${Ansi.BOLD}[ MEDIUM ]${Ansi.RESET}"
            Priority.LOW -> "${Ansi.CYAN}[ LOW ]${Ansi.RESET}"
        }
    }

    private fun formatStatusBadge(status: TaskStatus): String {
        return when (status) {
            TaskStatus.COMPLETED -> "${Ansi.GREEN}${Ansi.BOLD}[ COMPLETED ]${Ansi.RESET}"
            TaskStatus.IN_PROGRESS -> "${Ansi.YELLOW}${Ansi.BOLD}[ IN_PROGRESS ]${Ansi.RESET}"
            TaskStatus.NOT_STARTED -> "${Ansi.RED}[ NOT_STARTED ]${Ansi.RESET}"
        }
    }

    private fun displaySearchResults(results: List<Task>) {
        if (results.isEmpty()) {
            println("\n${Ansi.YELLOW}[i] Tidak ditemukan tugas yang sesuai kriteria.${Ansi.RESET}")
        } else {
            println("\n${Ansi.GREEN}[i] Ditemukan ${results.size} tugas:${Ansi.RESET}")
            results.forEach { renderTaskCard(it) }
        }
    }

    // ==========================================
    // HELPER INPUT ANTI-CRASH & UX
    // ==========================================

    private fun pausePrompt() {
        print("\n${Ansi.DIM}Tekan [ENTER] untuk kembali ke menu...${Ansi.RESET}")
        readlnOrNull()
    }

    private fun readInt(prompt: String): Int? {
        print(prompt)
        val input = readlnOrNull()?.trim()
        val number = input?.toIntOrNull()
        if (number == null) {
            println("${Ansi.RED}[!] Harap masukkan ANGKA yang valid.${Ansi.RESET}")
        }
        return number
    }

    private fun readNonBlankString(prompt: String): String {
        while (true) {
            print(prompt)
            val input = readlnOrNull()?.trim()
            if (!input.isNullOrBlank()) {
                return input
            }
            println("${Ansi.RED}[!] Kolom ini tidak boleh kosong! Coba lagi.${Ansi.RESET}")
        }
    }

    private fun readFutureDate(prompt: String): LocalDate {
        while (true) {
            val date = readDate(prompt)
            if (!date.isBefore(LocalDate.now())) {
                return date
            }
            println("${Ansi.RED}[!] Tanggal deadline tidak boleh di masa lalu!${Ansi.RESET}")
        }
    }

    private fun readDate(prompt: String): LocalDate {
        while (true) {
            print(prompt)
            val input = readlnOrNull()?.trim()
            if (input.isNullOrBlank()) {
                println("${Ansi.RED}[!] Tanggal tidak boleh kosong!${Ansi.RESET}")
                continue
            }
            try {
                return LocalDate.parse(input)
            } catch (_: DateTimeParseException) {
                println("${Ansi.RED}[!] Format tanggal salah! Gunakan format YYYY-MM-DD (Contoh: 2026-12-31).${Ansi.RESET}")
            }
        }
    }

    private fun readPriority(): Priority {
        while (true) {
            println("Pilih Tingkat Prioritas:")
            println(" 1. ${Ansi.CYAN}LOW${Ansi.RESET} (Tugas Santai)")
            println(" 2. ${Ansi.YELLOW}MEDIUM${Ansi.RESET} (Tugas Standar)")
            println(" 3. ${Ansi.RED}${Ansi.BOLD}HIGH${Ansi.RESET} (Mendesak / Susah)")
            when (readInt("Pilihan prioritas (1-3): ")) {
                1 -> return Priority.LOW
                2 -> return Priority.MEDIUM
                3 -> return Priority.HIGH
                else -> println("${Ansi.RED}[!] Pilih angka 1 sampai 3.${Ansi.RESET}")
            }
        }
    }

    private fun readStatus(): TaskStatus {
        while (true) {
            println("Pilih Status Pengerjaan:")
            println(" 1. ${Ansi.RED}NOT_STARTED${Ansi.RESET} (Belum disentuh)")
            println(" 2. ${Ansi.YELLOW}IN_PROGRESS${Ansi.RESET} (Sedang dikerjakan)")
            println(" 3. ${Ansi.GREEN}COMPLETED${Ansi.RESET} (Sudah selesai)")
            when (readInt("Pilihan status (1-3): ")) {
                1 -> return TaskStatus.NOT_STARTED
                2 -> return TaskStatus.IN_PROGRESS
                3 -> return TaskStatus.COMPLETED
                else -> println("${Ansi.RED}[!] Pilih angka 1 sampai 3.${Ansi.RESET}")
            }
        }
    }
}