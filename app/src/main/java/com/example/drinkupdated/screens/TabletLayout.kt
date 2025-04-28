package com.example.drinkupdated.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.drinkupdated.screens.CocktailListScreen
import com.example.drinkupdated.screens.DetailScreen
import com.example.drinkupdated.data.Cocktail

// Tablet Layout (Dual-Pane Navigation)
@Composable
fun TabletLayout(paddingValues: PaddingValues) {
    // Use rememberSaveable to persist the selected cocktail state across orientation changes
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)  // <-- Apply padding from Scaffold
    ) {
        // Left: Cocktail List
        Box(modifier = Modifier.weight(1f)) {
            CocktailListScreen { selectedCocktail = it }
        }
        // Right: Cocktail Details (or Placeholder)
        Box(
            modifier = Modifier
                .weight(2f)
                .padding(16.dp)
        ) {
            selectedCocktail?.let {
                DetailScreen(it) {}
            } ?: Text("Wybierz koktajl", style = MaterialTheme.typography.titleLarge)
        }
    }
}
