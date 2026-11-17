package com.studentmanager.service

import com.studentmanager.contracts.TaskOperations
import com.studentmanager.model.*
import java.time.LocalDate

class TaskManager : TaskOperations {

    private val tasks = mutableListOf<Task>()

    override fun addTask(task: Task): Boolean {

        if (tasks.any { it.id == task.id }) {
            return false
        }

        if (task.title.isBlank()) {
            return false
        }

        if (task.deadline.isBefore(LocalDate.now())) {
            return false
        }

        tasks.add(task)
        return true
    }

    override fun getAllTasks(): List<Task> {
        return tasks.toList()
    }

    override fun findTask(id: Int): Task? {
        return tasks.find { it.id == id }
    }

    override fun deleteTask(id: Int): Boolean {
        return tasks.removeIf { it.id == id }
    }

    fun editTask(
        id: Int,
        title: String,
        description: String,
        deadline: LocalDate,
        priority: Priority
    ): Boolean {

        val task = findTask(id) ?: return false

        if (title.isBlank()) return false
        if (deadline.isBefore(LocalDate.now())) return false

        task.title = title
        task.description = description
        task.deadline = deadline
        task.priority = priority

        return true
    }

    override fun updateTaskStatus(
        id: Int,
        status: TaskStatus
    ): Boolean {

        val task = findTask(id) ?: return false

        task.updateStatus(status)

        return true
    }

    fun findByTitle(keyword: String): List<Task> {
        return tasks.filter {
            it.title.contains(keyword, ignoreCase = true)
        }
    }

    fun findByDeadline(date: LocalDate): List<Task> {
        return tasks.filter {
            it.deadline == date
        }
    }

    fun getTasksSortedByDeadline(): List<Task> {
        return tasks.sortedBy { it.deadline }
    }

    fun showStatistics() {

        val total = tasks.size

        val completed = tasks.count {
            it.getStatus() == TaskStatus.COMPLETED
        }

        val inProgress = tasks.count {
            it.getStatus() == TaskStatus.IN_PROGRESS
        }

        val notStarted = tasks.count {
            it.getStatus() == TaskStatus.NOT_STARTED
        }

        println("\n======= STATISTIK TUGAS =======")
        println("Total tugas       : $total")
        println("Belum dimulai     : $notStarted")
        println("Sedang dikerjakan : $inProgress")
        println("Selesai           : $completed")

        val percentage = if (total > 0) {
            completed * 100.0 / total
        } else {
            0.0
        }

        println("Progres selesai   : %.1f%%".format(percentage))
    }

    fun loadTasks(data: List<Task>) {
        tasks.clear()
        tasks.addAll(data)
    }
}