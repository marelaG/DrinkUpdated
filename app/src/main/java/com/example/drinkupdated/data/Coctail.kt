package com.example.drinkupdated.data

import android.os.Parcelable

import kotlinx.parcelize.Parcelize


@Parcelize
data class Cocktail(val name: String, val ingredients: String, val recipe: String) : Parcelable
