package com.studentmanager.contracts

import com.studentmanager.model.Task
import com.studentmanager.model.TaskStatus

interface TaskOperations {

    fun addTask(task: Task): Boolean

    fun getAllTasks(): List<Task>

    fun findTask(id: Int): Task?

    fun deleteTask(id: Int): Boolean

    fun updateTaskStatus(
        id: Int,
        status: TaskStatus
    ): Boolean
}