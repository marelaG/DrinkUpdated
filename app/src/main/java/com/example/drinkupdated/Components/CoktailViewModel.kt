package com.example.drinkupdated.Components


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.drinkupdated.data.Cocktail

class CocktailViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set

    fun updateSearchQuery(newValue: String) {
        searchQuery = newValue
    }


}
