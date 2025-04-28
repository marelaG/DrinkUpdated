package com.example.drinkupdated.Components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "All",
            color=Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick("home") }
                .padding(8.dp)
        )
        Text(
            text = "Non-alcohol drinks",
            color=Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick("categories") }
                .padding(8.dp)

        )
        Text(
            text = "Alcoholic drinks",
            color=Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick("settings") }
                .padding(8.dp)
        )
    }
}
