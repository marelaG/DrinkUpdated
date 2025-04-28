package com.example.drinkupdated.screens

import android.os.Parcelable
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.drinkupdated.data.Cocktail
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.parcelize.Parcelize

// Cocktail data class, now Parcelable

// Cocktail List Screen
@OptIn(UnstableApi::class)
@Composable
fun CocktailListScreen(onCocktailSelected: (Cocktail) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var cocktails by remember { mutableStateOf<List<Cocktail>>(emptyList()) }

    // Persist selected cocktail across orientation changes
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    // Fetch data from Firestore
    LaunchedEffect(Unit) {
        db.collection("drinks") // Your Firestore collection name
            .get()
            .addOnSuccessListener { result ->
                val fetchedCocktails = result.documents.mapNotNull { document ->
                    val name = document.getString("name") ?: return@mapNotNull null
                    val ingredients = document.getString("ingredients") ?: return@mapNotNull null
                    val recipe = document.getString("recipe") ?: return@mapNotNull null
                    Cocktail(
                        name, ingredients, recipe
                    )
                }
                cocktails = fetchedCocktails
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreData", "Error getting drinks", e)
            }
    }

    // Display drinks from Firebase
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(cocktails) { cocktail ->
            val isSelected = cocktail == selectedCocktail

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedCocktail = if (isSelected) {
                            // Deselect the cocktail if it's already selected
                            null
                        } else {
                            cocktail
                        }
                        onCocktailSelected(cocktail)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = cocktail.name,
                    modifier = Modifier
                        .padding(16.dp)
                        .apply {
                            if (isSelected) {
                                // Highlight selected cocktail (change color or style)
                                this.background(color = androidx.compose.ui.graphics.Color.LightGray)
                            }
                        }
                )
            }
        }
    }
}
