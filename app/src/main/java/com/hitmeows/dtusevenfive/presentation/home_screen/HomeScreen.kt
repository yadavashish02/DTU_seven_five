package com.hitmeows.dtusevenfive.presentation.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hitmeows.dtusevenfive.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
	viewModel: HomeScreenViewModel
) {
	
	val state = viewModel.state.collectAsState()
	var isEdit by remember {
		mutableStateOf(false)
	}
	val ongoing = viewModel.ongoing.collectAsState()
	
	Scaffold(
		topBar = { TopAppBar() },
		floatingActionButton = {
			FAB {
				isEdit = !isEdit
			}
		},
		floatingActionButtonPosition = FabPosition.End
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		) {
			
			if (isEdit) {
				AddSub(onDone = {
					viewModel.addSubject(it)
					isEdit = false
				})
			}
			LazyColumn {
				items(state.value) {
					SubjectCard(
						subjectDto = it,
						onRemove = { viewModel.removeSubject(it.name) },
						onPresent = {viewModel.markPresent(it.name)},
						onAbsent = {viewModel.markAbsent(it.name)},
						onSubtractPresent = {viewModel.unmarkPresent(it.name)},
						onSubtractAbsent = {viewModel.unmarkAbsent(it.name)},
						isEnabled = !ongoing.value,
						modifier = Modifier
							.padding(10.dp, 5.dp)
					)
				}
				item {
					Spacer(modifier = Modifier.height(70.dp))
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
	CenterAlignedTopAppBar(
		title = {
			Text(text = stringResource(R.string.home_screen_title))
		}
	)
}

@Composable
fun FAB(
	onClick: () -> Unit
) {
	ExtendedFloatingActionButton(
		onClick = {
			onClick()
		}
	) {
		Text(text = "Add Subject")
		Icon(
			imageVector = Icons.Default.Add,
			contentDescription = ""
		)
	}
	
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSub(
	onDone: (String) -> Unit
) {
	var sub by remember {
		mutableStateOf("")
	}
	OutlinedTextField(
		value = sub,
		onValueChange = {sub=it},
		keyboardOptions = KeyboardOptions(
			imeAction = ImeAction.Done
		),
		keyboardActions = KeyboardActions(
			onDone = {
				if (sub.isNotBlank()) {
					onDone(sub)
				}
			}
		),
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp),
		placeholder = {
			Text(text = "enter new subject name")
		},
		singleLine = true
	)
}