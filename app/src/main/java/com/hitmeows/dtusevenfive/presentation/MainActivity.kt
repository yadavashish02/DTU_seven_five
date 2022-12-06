package com.hitmeows.dtusevenfive.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.hitmeows.dtusevenfive.data.AttendanceRepositoryImpl
import com.hitmeows.dtusevenfive.data.local.AttendanceDatabase
import com.hitmeows.dtusevenfive.domain.AttendanceRepository
import com.hitmeows.dtusevenfive.presentation.home_screen.Home
import com.hitmeows.dtusevenfive.presentation.home_screen.HomeScreenViewModel
import com.hitmeows.dtusevenfive.presentation.ui.theme.DTUSevenFiveTheme


class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val db = Room.databaseBuilder(
			this,
			AttendanceDatabase::class.java,
			AttendanceDatabase.DB_NAME
		).build()
		val repo: AttendanceRepository = AttendanceRepositoryImpl(db)
		
		val vm = HomeScreenViewModel(repo)
		
		
		setContent {
			DTUSevenFiveTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Home(vm)
				}
			}
		}
	}
}
