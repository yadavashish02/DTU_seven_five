package com.hitmeows.dtusevenfive.data

import com.hitmeows.dtusevenfive.data.local.AttendanceDatabase
import com.hitmeows.dtusevenfive.data.local.Subject
import com.hitmeows.dtusevenfive.domain.AttendanceRepository
import com.hitmeows.dtusevenfive.domain.SubjectDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AttendanceRepositoryImpl(
	db: AttendanceDatabase
): AttendanceRepository {
	private val dao = db.dao
	
	override suspend fun insertSubject(name: String) {
		if (isSubjectExists(name)) {
			throw SubjectAlreadyExistsException()
		}
		
		dao.insertSubject(
			Subject(
				name = name
			)
		)
		
	}
	
	override suspend fun deleteSubject(name: String) {
		if (!isSubjectExists(name)) {
			throw SubjectNotFoundException()
		}
		
		val subject = getSubject(name)
		dao.deleteSubject(subject)
	}
	
	override suspend fun addPresent(name: String) {
		if (!isSubjectExists(name)) {
			throw SubjectNotFoundException()
		}
		
		val subject = getSubject(name)
		val present = subject.present+1
		val total = subject.total+1
		
		val localdt = LocalDateTime.now()
		val offset = ZoneOffset.ofHoursMinutes(5,30)
		val epoch = localdt.toEpochSecond(offset)
		
		dao.updateSubject(
			Subject(
				name = name,
				present = present,
				total = total,
				lastMarked = epoch
			)
		)
	}
	
	override suspend fun addAbsent(name: String) {
		if (!isSubjectExists(name)) {
			throw SubjectNotFoundException()
		}
		
		val subject = getSubject(name)
		val present = subject.present
		val total = subject.total+1
		
		val localdt = LocalDateTime.now()
		val offset = ZoneOffset.ofHoursMinutes(5,30)
		val epoch = localdt.toEpochSecond(offset)
		
		dao.updateSubject(
			Subject(
				name = name,
				present = present,
				total = total,
				lastMarked = epoch
			)
		)
	}
	
	override suspend fun removePresent(name: String) {
		if (!isSubjectExists(name)) {
			throw SubjectNotFoundException()
		}
		
		val subject = getSubject(name)
		val present = subject.present-1
		val total = subject.total-1
		
		if (present<0 || total<0) {
			return
		}
		
		val localdt = LocalDateTime.now()
		val offset = ZoneOffset.ofHoursMinutes(5,30)
		val epoch = localdt.toEpochSecond(offset)
		
		dao.updateSubject(
			Subject(
				name = name,
				present = present,
				total = total,
				lastMarked = epoch
			)
		)
	}
	
	override suspend fun removeAbsent(name: String) {
		if (!isSubjectExists(name)) {
			throw SubjectNotFoundException()
		}
		
		val subject = getSubject(name)
		val present = subject.present
		val total = subject.total-1
		
		if (total<0) {
			return
		}
		
		val localdt = LocalDateTime.now()
		val offset = ZoneOffset.ofHoursMinutes(5,30)
		val epoch = localdt.toEpochSecond(offset)
		
		dao.updateSubject(
			Subject(
				name = name,
				present = present,
				total = total,
				lastMarked = epoch
			)
		)
	}
	
	@OptIn(ExperimentalCoroutinesApi::class)
	override fun getAllSubjects(): Flow<List<SubjectDto>> {
		return dao.getAllSubjects().mapLatest {
			it.map { subject ->
				subject.toDto()
			}
		}
	}
	
	
	private suspend fun isSubjectExists(name: String): Boolean {
		return dao.isSubject(name)>=1
	}
	
	private suspend fun getSubject(name: String): Subject {
		return dao.getSubject(name)
	}
	
	private fun Subject.toDto(): SubjectDto {
		val percent = if (total==0) {
			0.0f
		} else {
			present.toFloat().div(total.toFloat()).times(100f)
		}
		
		val toAttend = if (total==0 || percent>=75.0) {
			"0"
		} else {
			var p: Float
			var at = present
			var t = total
			var toAt = 0
			for (i in 1..10) {
				at++
				t++
				p = at.toFloat().div(t.toFloat()).times(100f)
				toAt++
				if (p>=75.0) {
					break
				}
			}
			if (toAt>9) {
				">9"
			}else {
				toAt.toString()
			}
		}
		
		val offset = ZoneOffset.ofHoursMinutes(5,30)
		
		val localdt = LocalDateTime.ofEpochSecond(
			lastMarked,
			0,
			offset
		)
		
		val hour = if (localdt.hour<10) {
			"0${localdt.hour}"
		} else {
			"${localdt.hour}"
		}
		
		val min = if (localdt.minute<10) {
			"0${localdt.minute}"
		} else {
			"${localdt.minute}"
		}
		
		val last = if (lastMarked==0L) {
			"Never"
		} else {
			"${localdt.dayOfMonth} ${localdt.month.name.substring(0,3)} at $hour:$min"
		}
		
		return SubjectDto(
			name = name,
			present = present,
			total = total,
			percent = percent,
			toAttendContinuously = toAttend,
			lastMarked = last
		)
	}
	
}

class SubjectAlreadyExistsException: Exception()
class SubjectNotFoundException: Exception()