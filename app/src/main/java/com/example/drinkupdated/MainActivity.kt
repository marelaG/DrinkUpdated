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
import androidx.compose.foundation.clickable

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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
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
        var currentScreen by rememberSaveable { mutableStateOf(0) } // 0 = "main", 1 = "alcoholFree", 2 = "alcoholic"

        // Use PagerState to track the swipe position
        val pagerState = rememberPagerState(initialPage = currentScreen)

        // Sync the Tab navigation with the swipe
        LaunchedEffect(pagerState.currentPage) {
            currentScreen = pagerState.currentPage
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(onItemClick = { item ->
                    scope.launch {
                        drawerState.close()
                        val pageIndex = when (item) {
                            "alcoholFree" -> 1
                            "alcoholic" -> 2
                            else -> 0
                        }
                        pagerState.animateScrollToPage(pageIndex)
                    }
                })

            },
            scrimColor = Color.Black.copy(alpha = 0.7f),
        ) {
            Scaffold(

                topBar = {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()) //
                    ) {

                        // Tabs for navigating between screens
                        TabRow(
                            selectedTabIndex = currentScreen,
                            modifier = Modifier.padding(horizontal = 0.dp)
                        ) {
                            Tab(
                                selected = currentScreen == 0,
                                onClick = { scope.launch {
                                    pagerState.animateScrollToPage(0)
                                } },
                                text = { Text("Main Info") }
                            )
                            Tab(
                                selected = currentScreen == 1,
                                onClick = { scope.launch {
                                    pagerState.animateScrollToPage(1)
                                } },
                                text = { Text("Non-Alcoholic") }
                            )
                            Tab(
                                selected = currentScreen == 2,
                                onClick = { scope.launch {
                                    pagerState.animateScrollToPage(2)
                                } },
                                text = { Text("Alcoholic") }
                            )
                        }

                        // Top AppBar
                        TopAppBar(
                            title = {
                                if (isSearchVisible) {
                                    SearchTextField(viewModel = viewModel)
                                } else {
                                    Text(
                                        when (currentScreen) {
                                            1 -> "Non-Alcoholic Drinks"
                                            2 -> "Alcoholic Drinks"
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
                // HorizontalPager to support swiping between screens
                HorizontalPager(
                    count = 3, // 3 screens: MainInfoCard, AlcoholFree, Alcoholic
                    state = pagerState,
                    //modifier = Modifier.padding(innerPadding)
                    modifier = Modifier.padding(top = 20.dp, bottom = 0.dp)
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> MainInfoCard(modifier = Modifier.padding(innerPadding))
                        1 -> AlcoholFreeCocktailListScreen(
                            searchQuery = viewModel.searchQuery,
                            modifier = Modifier.padding(innerPadding)
                        )
                        2 -> AlcoholCocktailListScreen(
                            onCocktailSelected = {},
                            searchQuery = viewModel.searchQuery,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
