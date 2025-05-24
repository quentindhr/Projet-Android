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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    private val qrScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val articleId = result.data?.getIntExtra("article_id", -1)
            if (articleId != -1) {
                // recherche l'article correspondant
                lifecycleScope.launch {
                    if (articleId != null) {
                        findArticleById(articleId)
                    }
                }
            }
        }
    }

    private suspend fun findArticleById(id: Int) {
        // appelle l'api pour obtenir les détails de l'article
        try {
            val article = ApiClient.api.getArticleById(id)
            // affiche les détails de l'article
            showArticleDetails(article)
        } catch (e: Exception) {
            Log.e("API", "Erreur lors de la recherche de l'article : ${e.message}")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
            val context = LocalContext.current

            // Lance l'appel aux articles de l'API au lancement de l'application
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
                    Row {
                        // bouton scan qr code
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(context, QRScannerActivity::class.java)
                                qrScannerLauncher.launch(intent)
                            },
                            containerColor = Color(0xFFE0E0E0),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.qr_code_icon),
                                contentDescription = "Scanner QR Code",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        
                        // bouton panier
                        FloatingActionButton(
                            onClick = {
                                context.startActivity(Intent(context, CartActivity::class.java))
                            },
                            containerColor = Color(0xFFE0E0E0),
                            contentColor = Color(0xFF000000),
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Panier"
                            )
                        }
                    }
                }
            ) { innerPadding ->
                ArticleList(articles = articles, modifier = Modifier.padding(innerPadding))
            }
        }
    }

    private fun showArticleDetails(article: Article) {
        val intent = Intent(this, ArticleDetailsActivity::class.java).apply {
            putExtra("article", article)
        }
        startActivity(intent)
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

