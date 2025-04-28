package com.example.drinkupdated.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.drinkupdated.data.Cocktail

@Composable
fun PhoneLayout(paddingValues: PaddingValues) {
    // Remember the selected cocktail state across recompositions and orientation changes
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (selectedCocktail == null) {
            CocktailListScreen { selectedCocktail = it }
        } else {
            DetailScreen(cocktail = selectedCocktail!!) { selectedCocktail = null }
        }
    }
}
