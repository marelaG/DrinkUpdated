package com.example.drinkupdated.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    onItemClick: (String) -> Unit, // Changed to accept a string parameter
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {

        Text(
            text = "Main Info",
            color=Color.White,
            modifier = Modifier
                .padding(16.dp)
                .clickable {onItemClick("Main info")  },
            style = MaterialTheme.typography.bodyLarge
        )
        // Add an item for alcohol-free drinks
        Text(
            text = "Alcohol-Free Drinks",
            color= Color.White,
            modifier = Modifier
                .padding(16.dp)
                .clickable { onItemClick("alcoholFree") },
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Alcohol Drinks",
            color= Color.White,
            modifier = Modifier
                .padding(16.dp)
                .clickable { onItemClick("alcoholic") },
            style = MaterialTheme.typography.bodyLarge
        )

    }
}