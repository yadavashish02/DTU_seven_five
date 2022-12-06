package com.hitmeows.dtusevenfive.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitmeows.dtusevenfive.data.SubjectAlreadyExistsException
import com.hitmeows.dtusevenfive.data.SubjectNotFoundException
import com.hitmeows.dtusevenfive.domain.AttendanceRepository
import com.hitmeows.dtusevenfive.domain.SubjectDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeScreenViewModel(
	private val repository: AttendanceRepository
): ViewModel() {
	private val _state = MutableStateFlow(listOf<SubjectDto>())
	val state = _state.asStateFlow()
	
	private val _loading = MutableStateFlow(false)
	val loading = _loading.asStateFlow()
	
	private val _error = MutableStateFlow("")
	val error = _error.asStateFlow()
	
	private val _ongoing = MutableStateFlow(false)
	val ongoing = _ongoing.asStateFlow()
	
	private var job: Job? = null
	private var markerJob: Job? = null
	private var subjectJob: Job? = null
	
	
	
	init {
		getAllSubjects()
	}
	
	private fun getAllSubjects() {
		job?.cancel()
		Log.d("meme","meme")
		job = viewModelScope.launch(Dispatchers.IO) {
			_loading.emit(true)
			repository.getAllSubjects().collectLatest {
				Log.d("meme",it.toString())
				_state.emit(it)
			}
			_loading.emit(false)
		}
	}
	
	fun markPresent(subjectName: String) {
		if (markerJob?.isActive == true) {
			return
		}
		markerJob = viewModelScope.launch(Dispatchers.IO) {
			_ongoing.emit(true)
			try {
				repository.addPresent(subjectName)
			} catch (e: SubjectNotFoundException) {
				_error.emit("subject does not exist")
			} catch (e: Exception) {
				_error.emit("unknown error occurred")
			}finally {
				delay(5000)
				_ongoing.emit(false)
			}
		}
	}
	
	fun cancelEvent() {
		markerJob?.cancel()
	}
	
	fun markAbsent(subjectName: String) {
		if (markerJob?.isActive == true) {
			return
		}
		markerJob = viewModelScope.launch(Dispatchers.IO) {
			_ongoing.emit(true)
			try {
				repository.addAbsent(subjectName)
			} catch (e: SubjectNotFoundException) {
				_error.emit("subject does not exist")
			} catch (e: Exception) {
				_error.emit("unknown error occurred")
			}finally {
				delay(5000)
				_ongoing.emit(false)
			}
		}
	}
	
	fun addSubject(subjectName: String) {
		if (subjectJob?.isActive == true) {
			Log.d("meme","active")
			return
		}
		
		subjectJob = viewModelScope.launch(Dispatchers.IO) {
			try {
				repository.insertSubject(subjectName)
				Log.d("meme","done")
			} catch (e: SubjectAlreadyExistsException) {
				_error.emit("subject name already taken")
			} catch (e: Exception) {
				_error.emit("unknown error occurred")
			}
		}
	}
	
	fun removeSubject(subjectName: String) {
		if (subjectJob?.isActive == true) {
			return
		}
		
		subjectJob = viewModelScope.launch(Dispatchers.IO) {
			try {
				repository.deleteSubject(subjectName)
			} catch (e: SubjectAlreadyExistsException) {
				_error.emit("subject name already taken")
			} catch (e: Exception) {
				_error.emit("unknown error occurred")
			}
		}
	}
	
	fun unmarkPresent(subjectName: String) {
		if (markerJob?.isActive == true) {
			return
		}
		markerJob = viewModelScope.launch(Dispatchers.IO) {
			_ongoing.emit(true)
			try {
				repository.removePresent(subjectName)
			} catch (e: SubjectNotFoundException) {
				_error.emit("subject does not exist")
			} catch (e: Exception) {
				_error.emit("unknown error occurred")
			}finally {
				delay(500)
				_ongoing.emit(false)
			}
		}
	}
	
	fun unmarkAbsent(subjectName: String) {
		if (markerJob?.isActive == true) {
			return
		}
		markerJob = viewModelScope.launch(Dispatchers.IO) {
			_ongoing.emit(true)
			try {
				repository.removeAbsent(subjectName)
			} catch (e: SubjectNotFoundException) {
				_error.emit("subject does not exist")
			} catch (e: Exception) {
				_error.emit("unknown error occurred")
			}finally {
				delay(500)
				_ongoing.emit(false)
			}
		}
	}
	
	
}

