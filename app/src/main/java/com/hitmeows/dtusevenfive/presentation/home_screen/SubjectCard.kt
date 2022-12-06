package com.hitmeows.dtusevenfive.presentation.home_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hitmeows.dtusevenfive.domain.SubjectDto

@Composable
fun SubjectCard(
	subjectDto: SubjectDto,
	onRemove: () -> Unit,
	onPresent: () -> Unit,
	onAbsent: () -> Unit,
	onSubtractPresent: () -> Unit,
	onSubtractAbsent: () -> Unit,
	isEnabled: Boolean,
	modifier: Modifier = Modifier
) {
	Box(modifier = modifier) {
		SubjectCardUtil(subject = subjectDto, { onRemove() }, { onPresent() }, { onAbsent() },{onSubtractPresent()},{onSubtractAbsent()},isEnabled)
	}
	
}

@Composable
fun Marker(
	lastMarked: String,
	onPresent: () -> Unit,
	onAbsent: () -> Unit,
	isEnabled: Boolean,
	isEdit: Boolean,
	onSubtractPresent: () -> Unit,
	onSubtractAbsent: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = "last marked: $lastMarked",
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.secondary.copy(0.5f)
		)
		if (!isEdit) {
			Row {
				TextButton(onClick = {
					onPresent()
				},
					enabled = isEnabled
				) {
					Text(text = "Present")
				}
				TextButton(onClick = {
					onAbsent()
				},
					enabled = isEnabled
				) {
					Text(text = "Absent")
				}
			}
		} else {
			Row {
				TextButton(onClick = {
					onSubtractPresent()
				},
					enabled = isEnabled
				) {
					Text(text = "P--")
				}
				TextButton(onClick = {
					onSubtractAbsent()
				},
					enabled = isEnabled
				) {
					Text(text = "A--")
				}
			}
		}
	}
}

@Composable
fun RemoveSubjectButton(
	onRemove: () -> Unit,
	modifier: Modifier = Modifier
) {
	IconButton(
		onClick = {
			onRemove()
		},
		modifier = modifier
	) {
		Icon(
			imageVector = Icons.Default.Delete,
			tint = MaterialTheme.colorScheme.error,
			contentDescription = ""
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectCardUtil(
	subject: SubjectDto,
	onRemove: () -> Unit,
	onPresent: () -> Unit,
	onAbsent: () -> Unit,
	onSubtractPresent: () -> Unit,
	onSubtractAbsent: () -> Unit,
	isEnabled: Boolean,
	modifier: Modifier = Modifier
) {
	var clicked by remember {
		mutableStateOf(false)
	}
	
	ElevatedCard(
		modifier
			.fillMaxWidth()
			.combinedClickable(
				onClick = {
					clicked = false
				},
				onLongClick = {
					clicked = !clicked
				}
			)
	) {
		Row(
			Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				Modifier.fillMaxWidth(0.65f),
				verticalAlignment = Alignment.CenterVertically
			) {
				CharIcon(
					char = subject.name[0],
					onRemove = {
						onRemove()
					},
					clicked = clicked,
					percent = subject.percent,
					modifier = Modifier.padding(10.dp)
				)
				Text(
					text = subject.name,
					style = MaterialTheme.typography.headlineMedium,
					color = MaterialTheme.colorScheme.secondary,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
			Column(
				Modifier
					.padding(start=10.dp,end=30.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "present: ${subject.present}",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.tertiary
				)
				Text(
					text = "required: ${subject.toAttendContinuously}",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.tertiary
				)
				Text(
					text = "classes: ${subject.total}",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.tertiary
				)
				
				
			}
		}
		
		Marker(
			lastMarked = subject.lastMarked,
			onPresent = { onPresent() },
			onAbsent = { onAbsent() },
			isEnabled = isEnabled,
			isEdit = clicked,
			onSubtractPresent = {onSubtractPresent()},
			onSubtractAbsent = {onSubtractAbsent()},
			modifier = Modifier
				.fillMaxWidth()
				.padding(5.dp)
		)
		
	}
}

@Composable
fun CharIcon(
	char: Char,
	onRemove: () -> Unit,
	clicked: Boolean,
	percent: Float,
	modifier: Modifier = Modifier
) {
	var showPercent by remember {
		mutableStateOf(false)
	}
	
	val bg = MaterialTheme.colorScheme.secondaryContainer
	val arcColor = MaterialTheme.colorScheme.onSecondaryContainer
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		
		Canvas(
			modifier = Modifier
				.size(70.dp)
				.clickable {
					showPercent = !showPercent
				}
		) {
			drawCircle(bg)
			drawArc(
				arcColor,
				-90f,
				3.6f * percent * (100f / 75f),
				false,
				style = Stroke(
					5.0f,
					cap = StrokeCap.Round
				)
			)
		}
		if (clicked) {
			IconButton(
				onClick = {
					onRemove()
				}
			) {
				Icon(
					imageVector = Icons.Default.Delete,
					contentDescription = "",
					tint = MaterialTheme.colorScheme.error
				)
			}
		} else {
			val txt = if (showPercent) String.format("%.2f", percent) else char.uppercase()
			Text(
				text = txt,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				style = MaterialTheme.typography.labelLarge
			)
		}
	}
}