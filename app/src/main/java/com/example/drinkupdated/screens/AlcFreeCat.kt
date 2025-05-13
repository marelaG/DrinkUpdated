package com.example.drinkupdated.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.ImageSource
import com.example.drinkupdated.data.Cocktail
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items



@OptIn(UnstableApi::class)
@Composable
fun AlcoholFreeCocktailListScreen(

    searchQuery: String,
    modifier: Modifier = Modifier,
) {
    val db = FirebaseFirestore.getInstance()
    var cocktails by remember { mutableStateOf<List<Cocktail>>(emptyList()) }
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    val isTabletDevice = isTablet()

    LaunchedEffect(true) {
        db.collection("drinks")
            .whereEqualTo("alco", false)
            .get()
            .addOnSuccessListener { result ->
                cocktails = result.documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val ingredients = doc.getString("ingredients") ?: return@mapNotNull null
                    val recipe = doc.getString("recipe") ?: return@mapNotNull null
                    val alco = doc.getBoolean("alco") ?: true
                    val imageUrl = doc.getString("image")
                    if (!alco) Cocktail(name, ingredients, recipe, alco, imageUrl) else null
                }
            }
            .addOnFailureListener { it.printStackTrace() }
    }

    val filteredCocktails = remember(cocktails, searchQuery) {
        if (searchQuery.isBlank()) {
            cocktails
        } else {
            cocktails.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.ingredients.contains(searchQuery, ignoreCase = true) ||
                        it.recipe.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        if (selectedCocktail != null && !isTabletDevice) {
            // Phone detail screen view
            DetailScreen(cocktail = selectedCocktail!!, onBack = { selectedCocktail = null })
            return
        }

        if (isTabletDevice) {
            if (selectedCocktail == null) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCocktails) { cocktail ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedCocktail = cocktail },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(8.dp)) {
                                cocktail.image?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentDescription = cocktail.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = cocktail.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "Alcohol-Free", color = Color.Green, style = MaterialTheme.typography.labelSmall)
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
            } else {
                Row(Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filteredCocktails) { cocktail ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { selectedCocktail = cocktail },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(text = cocktail.name, style = MaterialTheme.typography.headlineSmall)
                                    Text(text = "Alcohol-Free", color = Color.Green, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                        DetailScreen(cocktail = selectedCocktail!!, onBack = { selectedCocktail = null })
                    }
                }
            }
        } else {
            // Phone layout: list and click to show full detail
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredCocktails) { cocktail ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedCocktail = cocktail
                            },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            cocktail.image?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = cocktail.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                )
                            }
                            Text(text = cocktail.name, style = MaterialTheme.typography.headlineSmall)
                            Text(text = "Alcohol-Free", color = Color.Green, style = MaterialTheme.typography.labelSmall)
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
}
