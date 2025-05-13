package com.example.drinkupdated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.drinkupdated.Components.CocktailViewModel
import com.example.drinkupdated.Components.DrawerContent
import com.example.drinkupdated.screens.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                SplashScreen(onSplashFinished = {
                    showSplash = false
                })
            } else {
                MainDrinkAppUI()
            }
            //MainDrinkAppUI()
        }


    }
    }

    @Composable
    fun SearchTextField(viewModel: CocktailViewModel) {
        val searchQuery = viewModel.searchQuery

        TextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            label = { Text("Search Recipes") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {})
        )
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainDrinkAppUI() {
        val isDarkTheme = isSystemInDarkTheme()
        val viewModel: CocktailViewModel = viewModel()

        MaterialTheme(
            colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
        ) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            var isSearchVisible by remember { mutableStateOf(false) }
            var currentScreen by rememberSaveable { mutableStateOf("main") }

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerContent(onItemClick = { item ->
                        scope.launch { drawerState.close() }
                        currentScreen = when (item) {
                            "alcoholFree" -> "alcoholFree"
                            "alcoholic" -> "alcoholic"
                            else -> "main"
                        }
                    })
                },
                scrimColor = Color.Black.copy(alpha = 0.7f),
            ) {
                Scaffold(
                    topBar = {
                        Column {
                            TabRow(
                                selectedTabIndex = when (currentScreen) {
                                    "alcoholFree" -> 1
                                    "alcoholic" -> 2
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
                                    selected = currentScreen == "alcoholic",
                                    onClick = { currentScreen = "alcoholic" },
                                    text = { Text("Alcoholic") }
                                )
                            }

                            TopAppBar(
                                title = {
                                    if (isSearchVisible) {
                                        SearchTextField(viewModel = viewModel)
                                    } else {
                                        Text(
                                            when (currentScreen) {
                                                "alcoholFree" -> "Alcohol-Free Drinks"
                                                "alcoholic" -> "Alcoholic Drinks"
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

                            searchQuery = viewModel.searchQuery,
                            modifier = Modifier.padding(innerPadding)
                        )
                        "alcoholic" -> AlcoholCocktailListScreen(
                            onCocktailSelected = {},
                            searchQuery = viewModel.searchQuery,
                            modifier = Modifier.padding(innerPadding)
                        )
                        else -> MainInfoCard(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }


