package com.studentmanager

import com.studentmanager.model.Course
import com.studentmanager.model.Priority
import com.studentmanager.model.Task
import com.studentmanager.model.TaskStatus
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate

class TaskRepository(
    private val directory: Path = Paths.get("data")
) {
    private val coursesFile: Path = directory.resolve("courses.csv")
    private val tasksFile: Path = directory.resolve("tasks.csv")

    init {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory)
        }
    }

    fun save(courses: List<Course>, tasks: List<Task>) {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory)
        }

        val courseLines = courses.map { course ->
            "${course.id}|${sanitize(course.name)}|${sanitize(course.lecturerName)}"
        }
        Files.write(coursesFile, courseLines)

        val taskLines = tasks.map { task ->
            listOf(
                task.id,
                sanitize(task.title),
                sanitize(task.description),
                task.deadline.toString(),
                task.courseId,
                task.priority.name,
                task.status.name
            ).joinToString("|")
        }
        Files.write(tasksFile, taskLines)
    }

    fun loadCourses(): List<Course> {
        if (!Files.exists(coursesFile)) return emptyList()

        return Files.readAllLines(coursesFile)
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split("|")
                if (parts.size >= 3) {
                    Course(
                        id = parts[0],
                        name = parts[1],
                        lecturerName = parts[2]
                    )
                } else {
                    null
                }
            }
    }

    fun loadTasks(): List<Task> {
        if (!Files.exists(tasksFile)) return emptyList()

        return Files.readAllLines(tasksFile)
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split("|")
                if (parts.size >= 7) {
                    try {
                        Task(
                            id = parts[0].toInt(),
                            title = parts[1],
                            description = parts[2],
                            deadline = LocalDate.parse(parts[3]),
                            courseId = parts[4],
                            priority = Priority.valueOf(parts[5]),
                            status = TaskStatus.valueOf(parts[6])
                        )
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    null
                }
            }
    }

    private fun sanitize(text: String): String {
        return text.replace("|", "/").replace("\n", " ").replace("\r", "")
    }
}