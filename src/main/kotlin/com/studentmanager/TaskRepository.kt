package com.studentmanager

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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

    fun saveRawData(courseLines: List<String>, taskLines: List<String>) {
        Files.write(coursesFile, courseLines)
        Files.write(tasksFile, taskLines)
    }

    fun loadRawCourses(): List<String> {
        if (!Files.exists(coursesFile)) return emptyList()
        return Files.readAllLines(coursesFile).filter { it.isNotBlank() }
    }

    fun loadRawTasks(): List<String> {
        if (!Files.exists(tasksFile)) return emptyList()
        return Files.readAllLines(tasksFile).filter { it.isNotBlank() }
    }
}