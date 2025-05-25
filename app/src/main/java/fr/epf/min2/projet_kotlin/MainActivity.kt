package fr.epf.min2.projet_kotlin

import android.content.Context
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material.icons.rounded.PhoneIphone
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay



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
                        // bouton scan QR code
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

                var selectedCategory by remember { mutableStateOf<String?>(null) }

                val filteredArticles = if (selectedCategory == null) {
                    articles
                } else {
                    articles.filter { it.category == selectedCategory }
                }


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {


                    CategoryFilterBar(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selected ->
                            selectedCategory = if (selectedCategory == selected) null else selected
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    ArticleCarousel(articles = articles)

                    Spacer(modifier = Modifier.height(8.dp))


                    ArticleList(
                        articles = filteredArticles,
                        context = context
                    )

                }
            }
        }}

    private fun showArticleDetails(article: Article) {
        val intent = Intent(this, ArticleDetailsActivity::class.java).apply {
            putExtra("article", article)
        }
        startActivity(intent)
    }
}



@Composable
fun ArticleList(articles: List<Article>, context: Context, modifier: Modifier = Modifier) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = modifier
            .padding(8.dp)
    ) {
        items(articles.size) { index ->
            val article = articles[index]
            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxSize()
                    .clickable {
                    val intent = Intent(context, ArticleDetailsActivity::class.java).apply {
                        putExtra("article", article)
                    }
                    context.startActivity(intent)
                }
                ,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF9F9F9)
                )
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    AsyncImage(
                        model = article.image,
                        contentDescription = "Image de ${article.title}",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 16.dp)
                    )

                    Column {
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${article.price} €",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Text(
                            text = article.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterBar(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        "women's clothing" to (Icons.Rounded.Female to "Femme"),
        "men's clothing" to (Icons.Default.Male to "Homme"),
        "jewelery" to (Icons.Default.Star to "Bijoux"),
        "electronics" to (Icons.Default.PhoneIphone to "Électronique")
    )

    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        categories.forEach { (key, pair) ->
            val (icon, label) = pair
            val isSelected = selectedCategory == key

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onCategorySelected((if (isSelected) null else key).toString()) }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color(0xFF1976D2) else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) Color(0xFF1976D2) else Color.Gray
                )
            }


        }
    }
}

@Composable
fun ArticleCarousel(articles: List<Article>) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(articles) {
        if (articles.isNotEmpty()) {
            while (true) {
                delay(3000)
                currentIndex = (currentIndex + 1) % articles.size
                coroutineScope.launch {
                    listState.animateScrollToItem(currentIndex)
                }
            }
        }
    }



    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(articles.take(5)) { article ->
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(360.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF9F9F9)
                )

            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(article.image),
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2
                    )
                }
            }
        }
    }
}


