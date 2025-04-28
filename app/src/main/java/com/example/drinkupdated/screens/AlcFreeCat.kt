import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.drinkupdated.data.Cocktail
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CocktailGrid(cocktails: List<Cocktail>, onCocktailClick: (Cocktail) -> Unit) {
    // LazyVerticalGrid for displaying the cocktails in a grid layout
//    LazyVerticalGrid(
//        columns = 2, // This is the number of columns in the grid (you can adjust as needed)
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(cocktails) { cocktail ->
//            CocktailCard(cocktail, onClick = { onCocktailClick(cocktail) })
//        }
//    }
}

@Composable
fun CocktailCard(cocktail: Cocktail, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Image of the cocktail
//            Image(
//                //painter = painterResource(id = cocktail.imageResId),
//                contentDescription = cocktail.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//            )

            // Cocktail name
            Text(
                text = cocktail.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
@Composable
fun AlcFree(cocktails: List<Cocktail>, onCocktailClick: (Cocktail) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        CocktailGrid(cocktails = cocktails, onCocktailClick = onCocktailClick)
    }
}
