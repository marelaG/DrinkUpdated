package com.example.drinkupdated.screens

import android.os.Parcelable
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.drinkupdated.data.Cocktail
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(UnstableApi::class)
@Composable
fun AlcoholFreeCocktailListScreen(onCocktailSelected: (Cocktail) -> Unit,
                                  modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    var cocktails by remember { mutableStateOf<List<Cocktail>>(emptyList()) }

    // State for search query
    var searchQuery by remember { mutableStateOf("") }

    // Persist selected cocktail across orientation changes
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    // Fetch only alcohol-free drinks from Firestore
    LaunchedEffect(Unit) {
        db.collection("drinks")
            .whereEqualTo("alco", false) // Filter for non-alcoholic drinks
            .get()
            .addOnSuccessListener { result ->
                val fetchedCocktails = result.documents.mapNotNull { document ->
                    val name = document.getString("name") ?: return@mapNotNull null
                    val ingredients = document.getString("ingredients") ?: return@mapNotNull null
                    val recipe = document.getString("recipe") ?: return@mapNotNull null
                    val alco = document.getBoolean("alco") ?: true // Default to true if field missing

                    // Only include if explicitly marked as non-alcoholic
                    if (!alco) {
                        Cocktail(name, ingredients, recipe, alco)
                    } else {
                        null
                    }
                }
                cocktails = fetchedCocktails
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    // Filter cocktails based on the search query
    val filteredCocktails = cocktails.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.ingredients.contains(searchQuery, ignoreCase = true) ||
                it.recipe.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = modifier.padding(start = 16.dp, end = 16.dp).padding(top = 16.dp)
    ) {
        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Alcohol-Free Cocktails") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Perform any additional actions when search is triggered
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display filtered cocktails in a LazyColumn
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(filteredCocktails) { cocktail ->
                val isSelected = cocktail == selectedCocktail

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedCocktail = if (isSelected) {
                                null
                            } else {
                                cocktail
                            }
                            onCocktailSelected(cocktail)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = cocktail.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        // Optional: Add a tag to indicate it's alcohol-free
                        Text(
                            text = "Alcohol-Free",
                            color = Color.Green,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            if (filteredCocktails.isEmpty()) {
                item {
                    Text(
                        text = "No alcohol-free cocktails found.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
