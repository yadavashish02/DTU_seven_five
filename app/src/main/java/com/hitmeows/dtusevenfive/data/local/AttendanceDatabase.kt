package com.hitmeows.dtusevenfive.data.local

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(
	entities = [Subject::class],
	version = 1
)
abstract class AttendanceDatabase: RoomDatabase() {
	abstract val dao: AttendanceDao
	
	companion object {
		const val DB_NAME = "attendance_db"
	}
}