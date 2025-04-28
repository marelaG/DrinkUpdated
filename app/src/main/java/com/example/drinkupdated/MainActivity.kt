package com.example.drinkupdated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.example.drinkupdated.Components.DrawerContent
import com.example.drinkupdated.screens.PhoneLayout
import com.example.drinkupdated.screens.TabletLayout
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val isTablet = LocalConfiguration.current.screenWidthDp > 600

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerContent(onItemClick = {
                        scope.launch { drawerState.close() }
                        // handle navigation here if needed
                        //For example, if you have a navController:
                        //AlcFree() { }
                    })
                },
                scrimColor = Color.Black.copy(alpha = 0.7f),  // Change opacity to less transparent (0.7f)

            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Drink App") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    if (isTablet) {
                        TabletLayout(innerPadding) // pass innerPadding
                    } else {
                        PhoneLayout(innerPadding)  // pass innerPadding
                    }
                }
            }
        }
    }
}
