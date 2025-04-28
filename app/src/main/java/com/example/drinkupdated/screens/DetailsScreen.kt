package com.example.drinkupdated.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinkupdated.Components.AlertDialogExample
import com.example.drinkupdated.Pause
import com.example.drinkupdated.TimerViewModel
import com.example.drinkupdated.data.Cocktail
import com.example.drinkupdated.utils.formatTime


// Detail Screen

@Composable
fun DetailScreen(cocktail: Cocktail, onBack: () -> Unit) {
    // Remember the selected cocktail across orientation changes
    var savedCocktail by rememberSaveable { mutableStateOf(cocktail) }

    // Get an instance of TimerViewModel using `viewModel()` method
    val timerViewModel: TimerViewModel = viewModel()
    val openAlertDialog = remember { mutableStateOf(false) }

    // Collect the timer value from ViewModel
    val timerValue by timerViewModel.timer.collectAsState()

    // Variable to control if the user is editing the timer
    var isEditing by remember { mutableStateOf(false) }
    var editedTime by remember { mutableStateOf(timerValue.toString()) }

    // Start the timer immediately when the screen is shown
    LaunchedEffect(Unit) {
        // You can start the timer here if necessary
        // timerViewModel.startTimer()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = savedCocktail.name, style = MaterialTheme.typography.titleLarge)

            FloatingActionButton(
                onClick = {
                    openAlertDialog.value = true
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Recipe")
            }

            if (openAlertDialog.value) {
                AlertDialogExample(
                    onDismissRequest = {
                        openAlertDialog.value = false
                    },
                    onConfirmation = {
                        openAlertDialog.value = false
                    },
                    dialogText = "Sent!",
                    dialogTitle = "Send recipe via SMS",
                    icon = Icons.Default.Send
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Ingredients: ${savedCocktail.ingredients}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Recipe: ${savedCocktail.recipe}", style = MaterialTheme.typography.bodyLarge)

        // Timer Display - This is the part to allow editing
        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            // If editing, show a TextField to modify the timer value
            TextField(
                value = editedTime,
                onValueChange = { newTime ->
                    // Allow only numbers
                    if (newTime.toLongOrNull() != null || newTime.isEmpty()) {
                        editedTime = newTime
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                label = { Text("Enter time in seconds") },
                keyboardActions = KeyboardActions(onDone = {
                    // When the user finishes editing, update the timer and stop editing
                    val timeInSeconds = editedTime.toLongOrNull()
                    if (timeInSeconds != null) {
                        timerViewModel.setTimerTime(timeInSeconds) // Update the ViewModel with the new time
                        isEditing = false
                    }
                }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        } else {
            // If not editing, show the timer value as text
            Text(
                text = formatTime(timerValue.toInt()), // Convert Long to Int for formatTime
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        // When the user clicks on the timer, allow editing
                        isEditing = true
                        editedTime = timerValue.toString() // Set the current timer value in the input field
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Controls for the timer
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            // Start Button
            Button(onClick = {
                // If the time is updated, make sure we start from that new value
                val timeInSeconds = editedTime.toLongOrNull() ?: timerValue.toLong()
                timerViewModel.setTimerTime(timeInSeconds)  // Ensure the timer has the updated time
                timerViewModel.startTimer() // Start the timer
            }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Pause Button
            Button(onClick = { timerViewModel.pauseTimer() }) {
                Icon(
                    imageVector = Pause,
                    contentDescription = "Pause",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Stop Button
            Button(onClick = { timerViewModel.stopTimer() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Stop",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Restart Button
            Button(onClick = {
                timerViewModel.stopTimer()
                val timeInSeconds = editedTime.toLongOrNull() ?: timerValue.toLong()
                timerViewModel.setTimerTime(timeInSeconds) // Ensure the timer has the updated time
                timerViewModel.startTimer()
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(200.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}

