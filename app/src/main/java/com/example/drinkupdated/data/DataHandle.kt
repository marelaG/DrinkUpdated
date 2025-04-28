import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch

// Define your Cocktail data class (already provided)
data class Cocktail(val name: String, val ingredients: String, val recipe: String)

fun addCocktailsToFirestore() {
    val db = FirebaseFirestore.getInstance()

    // Initialize a batch
    val batch: WriteBatch = db.batch()

    // List of cocktail objects to add
    val cocktails = listOf(
        Cocktail("Margarita", "2 oz Tequila, 1 oz Lime juice, 1 oz Triple sec, Salt for rimming", "Rub a lime wedge around the rim of a glass, dip it into salt. In a shaker, mix tequila, lime juice, and triple sec with ice. Strain into the glass and serve."),
        Cocktail("Mojito", "2 oz White rum, 1 oz Lime juice, 2 teaspoons Sugar, Mint leaves, Soda water", "Muddle mint leaves and sugar in a glass. Add lime juice, rum, and ice, then top with soda water. Stir gently and garnish with mint."),
        Cocktail("Old Fashioned", "2 oz Bourbon, 1 Sugar cube, 2 dashes Angostura bitters, Orange peel", "Place a sugar cube in a glass and add bitters. Muddle with a splash of water, then add bourbon and ice. Stir well and garnish with orange peel."),
        Cocktail("Pina Colada", "2 oz White rum, 1 oz Coconut cream, 1 oz Pineapple juice, Pineapple slice for garnish", "Blend rum, coconut cream, and pineapple juice with ice until smooth. Pour into a glass and garnish with a pineapple slice."),
        Cocktail("Cosmopolitan", "1.5 oz Vodka, 1 oz Triple sec, 0.5 oz Lime juice, 0.5 oz Cranberry juice", "Shake all ingredients with ice, strain into a chilled martini glass, and garnish with a lime wheel."),
        Cocktail("Bloody Mary", "2 oz Vodka, 4 oz Tomato juice, 1 tbsp Lemon juice, 2 dashes Worcestershire sauce, 2 dashes Hot sauce, Celery salt, Pepper", "Shake vodka, tomato juice, lemon juice, Worcestershire sauce, hot sauce, celery salt, and pepper with ice. Strain into a glass and garnish with celery."),
        Cocktail("Negroni", "1 oz Gin, 1 oz Campari, 1 oz Sweet vermouth, Orange slice", "Stir gin, Campari, and vermouth with ice. Strain into a glass with a large ice cube and garnish with an orange slice."),
        Cocktail("Whiskey Sour", "2 oz Bourbon, 0.75 oz Lemon juice, 0.5 oz Simple syrup, Lemon twist", "Shake bourbon, lemon juice, and simple syrup with ice. Strain into a glass and garnish with a lemon twist."),
        Cocktail("Mai Tai", "1.5 oz White rum, 0.5 oz Dark rum, 0.5 oz Lime juice, 0.5 oz Orgeat syrup, 0.25 oz Orange Curacao", "Shake white rum, lime juice, orgeat syrup, and orange curacao with ice. Strain into a glass with crushed ice, then float dark rum on top."),
        Cocktail("Gin Tonic", "2 oz Gin, 4 oz Tonic water, Lime wedge", "Pour gin over ice in a glass, top with tonic water, and garnish with a lime wedge."),
        Cocktail("Long Island Iced Tea", "0.5 oz Vodka, 0.5 oz Tequila, 0.5 oz Rum, 0.5 oz Triple sec, 1 oz Lime juice, 1 oz Simple syrup, Splash of Cola", "Shake vodka, tequila, rum, triple sec, lime juice, and simple syrup with ice. Strain into a glass filled with ice and top with cola."),
        Cocktail("Caipirinha", "2 oz Cachaça, 1 Lime, 2 teaspoons Sugar", "Muddle lime and sugar in a glass. Add cachaça and ice, stir well, and serve."),
        Cocktail("Tequila Sunrise", "2 oz Tequila, 4 oz Orange juice, 0.5 oz Grenadine", "Pour tequila and orange juice into a glass over ice. Slowly add grenadine, allowing it to settle at the bottom. Do not stir."),
    )

    // Add each cocktail to the batch
    cocktails.forEach { cocktail ->
        val cocktailRef = db.collection("drinks").document() // Auto-generated ID
        batch.set(cocktailRef, cocktail)
    }

    // Commit the batch
    batch.commit()
        .addOnSuccessListener {
            // Batch write was successful
            println("All cocktails added to Firestore successfully!")
        }
        .addOnFailureListener { e ->
            // Error occurred while adding cocktails
            println("Error adding cocktails: $e")
        }
}
