package com.hitmeows.dtusevenfive.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface AttendanceDao {
	@Insert(onConflict = OnConflictStrategy.ABORT)
	suspend fun insertSubject(subject: Subject)
	
	@Delete
	suspend fun deleteSubject(subject: Subject)
	
	@Query("select * from subject")
	fun getAllSubjects(): Flow<List<Subject>>
	
	@Query("select count(*) from subject where name = :name")
	suspend fun isSubject(name: String): Int
	
	@Update
	suspend fun updateSubject(subject: Subject)
	
	@Query("select * from subject where name = :name")
	suspend fun getSubject(name: String): Subject
}