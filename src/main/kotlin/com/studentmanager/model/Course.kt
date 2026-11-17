package com.studentmanager.model

/** Data mata kuliah yang dapat dirujuk oleh tugas melalui courseId. */
data class Course(
    val id: String,
    val name: String,
    val lecturerName: String
)
