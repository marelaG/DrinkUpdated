package com.example.drinkupdated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.drinkupdated.Components.DrawerContent
import com.example.drinkupdated.screens.AlcoholCocktailListScreen
import com.example.drinkupdated.screens.AlcoholFreeCocktailListScreen
import com.example.drinkupdated.screens.PhoneLayout
import com.example.drinkupdated.screens.TabletLayout
import kotlinx.coroutines.launch
import com.example.drinkupdated.screens.PhoneLayout
import com.example.drinkupdated.screens.MainInfoCard
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme = isSystemInDarkTheme()

            MaterialTheme(
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                // All your existing content goes here
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val isTablet = LocalConfiguration.current.screenWidthDp > 600

                var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
                var isSearchVisible by remember { mutableStateOf(false) }
                var currentScreen by remember { mutableStateOf("main") }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(onItemClick = { item ->
                            scope.launch { drawerState.close() }
                            if (item == "alcoholFree") currentScreen = "alcoholFree"
                            else if (item == "alcoholic") currentScreen = "alcoholic"
                            else currentScreen = "main"
                        })
                    },
                    scrimColor = Color.Black.copy(alpha = 0.7f),
                ) {
                    Scaffold(
                        topBar = {
                            Column {
                                // Tab row for navigation
                                TabRow(
                                    selectedTabIndex = when (currentScreen) {
                                        "alcoholFree" -> 1
                                        "alcoholic" -> 2 // Fixed tab index for alcoholic screen
                                        else -> 0
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    Tab(
                                        selected = currentScreen == "main",
                                        onClick = { currentScreen = "main" },
                                        text = { Text("Main Info") }
                                    )
                                    Tab(
                                        selected = currentScreen == "alcoholFree",
                                        onClick = { currentScreen = "alcoholFree" },
                                        text = { Text("Non-Alcoholic") }
                                    )
                                    Tab(
                                        selected = currentScreen == "alcoholic",  // Fixed tab condition
                                        onClick = { currentScreen = "alcoholic" }, // Changed to "alcoholic"
                                        text = { Text("Alcoholic") }
                                    )
                                }

                                // Top app bar with search functionality
                                TopAppBar(
                                    title = {
                                        if (isSearchVisible) {
                                            SearchTextField(value = searchQuery) { searchQuery = it }
                                        } else {
                                            Text(
                                                when (currentScreen) {
                                                    "alcoholFree" -> "Alcohol-Free Drinks"
                                                    "alcoholic" -> "Alcoholic Drinks" // Fixed to alcoholic
                                                    else -> "Drink App"
                                                }
                                            )
                                        }
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch { drawerState.open() }
                                        }) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = {
                                            isSearchVisible = !isSearchVisible
                                            if (!isSearchVisible) searchQuery = TextFieldValue("")
                                        }) {
                                            Icon(Icons.Default.Search, contentDescription = "Search")
                                        }
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        when (currentScreen) {
                            "alcoholFree" -> AlcoholFreeCocktailListScreen(
                                onCocktailSelected = {},
                                modifier = Modifier.padding(innerPadding)
                            )
                            "alcoholic" -> {  // Fixed to show alcoholic screen
                                AlcoholCocktailListScreen(onCocktailSelected = {},
                                    modifier = Modifier.padding(innerPadding))
                            }
//                            else -> {
//                                if (isTablet) TabletLayout(innerPadding)
//                                else PhoneLayout(innerPadding)
//                            }
                            else ->{
                                MainInfoCard(modifier= Modifier.padding(innerPadding))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SearchTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.padding(start = 16.dp),
            label = { Text("Search Recipes") },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Implement search functionality here if needed
                }
            )
        )
    }
}
