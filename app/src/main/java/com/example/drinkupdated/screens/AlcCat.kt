package com.example.drinkupdated.screens


import android.os.Parcelable
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberAsyncImagePainter
import com.example.drinkupdated.Components.CocktailViewModel
import com.example.drinkupdated.data.Cocktail
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items


@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}


@Composable
fun AlcoholCocktailListScreen(
    onCocktailSelected: (Cocktail) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val db = FirebaseFirestore.getInstance()
    var cocktails by remember { mutableStateOf<List<Cocktail>>(emptyList()) }
    var selectedCocktail by rememberSaveable { mutableStateOf<Cocktail?>(null) }

    val isTabletDevice = isTablet()

    // Fetch cocktails from Firestore
    LaunchedEffect(Unit) {
        db.collection("drinks")
            .whereEqualTo("alco", true)
            .get()
            .addOnSuccessListener { result ->
                cocktails = result.documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val ingredients = doc.getString("ingredients") ?: return@mapNotNull null
                    val recipe = doc.getString("recipe") ?: return@mapNotNull null
                    val alco = doc.getBoolean("alco") ?: false
                    val image = doc.getString("image") ?: return@mapNotNull null
                    if (alco) Cocktail(name, ingredients, recipe, alco, image) else null
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

    val gridColumns = if (isTabletDevice) {
        GridCells.Adaptive(minSize = 150.dp)
    } else {
        GridCells.Fixed(1) // Enforce one item per row on phone
    }

    Column(modifier = modifier.padding(16.dp)) {
        if (selectedCocktail == null) {
            LazyVerticalGrid(
                columns = gridColumns,
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
                            cocktail.image?.let { imageUrl ->
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = cocktail.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = cocktail.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Alcoholic", color = Color.Red, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                if (filteredCocktails.isEmpty()) {
                    item {
                        Text(
                            text = "No alcoholic cocktails found.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        } else {
            // Show details
            if (isTabletDevice) {
                Row(Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = gridColumns,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredCocktails) { cocktail ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCocktail = cocktail },
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(Modifier.padding(8.dp)) {
                                    cocktail.image?.let { imageUrl ->
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUrl),
                                            contentDescription = cocktail.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(120.dp)
                                        )
                                    }
                                    Text(text = cocktail.name, style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Alcoholic", color = Color.Red, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)) {
                        DetailScreen(cocktail = selectedCocktail!!, onBack = { selectedCocktail = null })
                    }
                }
            } else {
                // Phone: show detail screen only
                DetailScreen(cocktail = selectedCocktail!!, onBack = { selectedCocktail = null })
            }
        }
    }
}
