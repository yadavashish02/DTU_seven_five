package com.hitmeows.dtusevenfive.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subject(
	@PrimaryKey(autoGenerate = false)
	val name: String,
	val present: Int = 0,
	val total: Int = 0,
	val lastMarked: Long = 0
)
