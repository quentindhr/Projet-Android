package fr.epf.min2.projet_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import fr.epf.min2.projet_kotlin.model.Article
import android.util.Log

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
            val context = LocalContext.current

            // Lancer une fois l'appel API
            LaunchedEffect(Unit) {
                try {
                    val result = ApiClient.api.getAllArticles()
                    articles = result
                } catch (e: Exception) {
                    Log.e("API", "Erreur lors de l'appel API : ${e.message}")
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            context.startActivity(Intent(context, CartActivity::class.java))
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Panier"
                        )
                    }
                }
            ) { innerPadding ->
                ArticleList(articles = articles, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun ArticleList(articles: List<Article>, modifier: Modifier = Modifier) {
    androidx.compose.foundation.lazy.LazyColumn(modifier = modifier) {
        items(articles.size) { index ->
            Text(text = articles[index].title)
        }
    }
}

