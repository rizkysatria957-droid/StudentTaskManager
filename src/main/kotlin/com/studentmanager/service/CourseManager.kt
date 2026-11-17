package com.studentmanager.service

import com.studentmanager.model.Course

class CourseManager {

    private val courses = mutableListOf<Course>()

    fun addCourse(course: Course): Boolean {

        if (courses.any { it.id == course.id }) {
            return false
        }

        if (course.name.isBlank()) {
            return false
        }

        courses.add(course)
        return true
    }

    fun getAllCourses(): List<Course> {
        return courses.toList()
    }

    fun findCourse(id: String): Course? {
        return courses.find { it.id == id }
    }

    fun showCourses() {

        if (courses.isEmpty()) {
            println("Belum ada mata kuliah.")
            return
        }

        courses.forEach {
            println("${it.id} - ${it.name} (${it.lecturerName})")
        }
    }

    fun loadCourses(data: List<Course>) {
        courses.clear()
        courses.addAll(data)
    }
}