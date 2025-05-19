package fr.epf.min2.projet_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.epf.min2.projet_kotlin.ui.theme.Projet_KotlinTheme
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import fr.epf.min2.projet_kotlin.APIClient
import fr.epf.min2.projet_kotlin.ApiService
import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Projet_KotlinTheme {
                var articles by remember { mutableStateOf<List<Article>>(emptyList()) }

                // Lancer une fois l’appel API
                LaunchedEffect(Unit) {
                    try {
                        val result = APIClient.api.getAllArticles()
                        articles = result
                    } catch (e: Exception) {
                        Log.e("API", "Erreur lors de l'appel API : ${e.message}")
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ArticleList(articles = articles, modifier = Modifier.padding(innerPadding))
                }
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

