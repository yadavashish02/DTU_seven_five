package com.hitmeows.dtusevenfive.domain

import java.time.LocalDateTime

data class SubjectDto(
	val name: String,
	val present: Int,
	val total: Int,
	val percent: Float,
	val toAttendContinuously: String,
	val lastMarked: String
)