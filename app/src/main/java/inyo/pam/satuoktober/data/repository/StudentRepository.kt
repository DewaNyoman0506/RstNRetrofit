package inyo.pam.satuoktober.data.repository

import inyo.pam.satuoktober.data.model.Student

interface StudentRepository {
    fun getStudents(callback: (Result<List<Student>>) -> Unit)
    fun getStudent(id: Int, callback: (Result<Student>) -> Unit)

    fun searchStudents(
        query: String,
        callback: (Result<List<Student>>) -> Unit
    )
}

