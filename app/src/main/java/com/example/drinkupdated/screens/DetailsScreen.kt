package com.example.drinkupdated.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinkupdated.Components.AlertDialogExample
import com.example.drinkupdated.icons.Pause
import com.example.drinkupdated.TimerViewModel
import com.example.drinkupdated.data.Cocktail
import com.example.drinkupdated.utils.formatTime


import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import coil.compose.rememberAsyncImagePainter


@Composable
fun DetailScreen(cocktail: Cocktail, onBack: () -> Unit) {
    // Remember the selected cocktail across orientation changes
    val savedCocktail = cocktail

    // Get an instance of TimerViewModel using `viewModel()` method
    val timerViewModel: TimerViewModel = viewModel()
    val openAlertDialog = remember { mutableStateOf(false) }

    // Collect the timer value from ViewModel
    val timerValue by timerViewModel.timer.collectAsState()

    // Start the timer immediately when the screen is shown


    // Create a ScrollState
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)  // Make the column scrollable
    ) {

            Image(painter = rememberAsyncImagePainter(savedCocktail.image),
                contentDescription = cocktail.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = savedCocktail.name,
                    style = MaterialTheme.typography.titleLarge
                )


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

        // Timer Display
        // Timer Input
        var timeInput by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            androidx.compose.material3.TextField(
                value = timeInput,
                onValueChange = { timeInput = it },
                label = { Text("Set time (sec)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = {
                    val seconds = timeInput.toLongOrNull() ?: 0L
                    timerViewModel.setTimer(seconds)
                }
            ) {
                Text("Set")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = formatTime(timerValue.toInt()), // Convert Long to Int for formatTime
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            // Start Button
            Button(onClick = { timerViewModel.startTimer() }) {
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
            Button(onClick = { timerViewModel.resetTimer(); timerViewModel.startTimer() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}
