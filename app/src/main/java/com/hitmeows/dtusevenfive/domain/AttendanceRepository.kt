package com.hitmeows.dtusevenfive.domain

import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
	suspend fun insertSubject(name: String)
	suspend fun deleteSubject(name: String)
	suspend fun addPresent(name: String)
	suspend fun addAbsent(name: String)
	suspend fun removePresent(name: String)
	suspend fun removeAbsent(name: String)
	
	fun getAllSubjects(): Flow<List<SubjectDto>>
}