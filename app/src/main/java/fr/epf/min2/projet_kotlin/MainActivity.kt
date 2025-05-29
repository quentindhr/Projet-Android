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
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import fr.epf.min2.projet_kotlin.components.CartButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.Dp

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
            runOnUiThread {
                showArticleDetails(article)
            }
        } catch (e: Exception) {
            Log.e("API", "Erreur lors de la recherche de l'article : ${e.message}")
            // affiche un message d'erreur à l'utilisateur
            runOnUiThread {
                Toast.makeText(this, "Article non trouvé", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
            var selectedCategory by remember { mutableStateOf<String?>(null) }
            var searchQuery by remember { mutableStateOf("") }
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(40.dp)
                                )


                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    CategoryFilterBar(
                                        selectedCategory = selectedCategory,
                                        onCategorySelected = { selected ->
                                            selectedCategory = if (selectedCategory == selected) null else selected
                                        },
                                        iconSize = 32.dp
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                }
                ,

                        floatingActionButton = {
                    Row {
                        // bouton scan QR code
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(context, QRScannerActivity::class.java)
                                qrScannerLauncher.launch(intent)
                            },
                            containerColor = Color(0xFFFF6B00),
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.qr_code_icon),
                                contentDescription = "Scanner QR Code",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        
                        // bouton panier avec notification
                        CartButton(context)
                    }
                }
            ) { innerPadding ->

                val filteredArticles = articles.filter { article ->
                    (selectedCategory == null || article.category == selectedCategory) &&
                            (searchQuery.isBlank() || article.title.contains(searchQuery, ignoreCase = true) || article.description.contains(searchQuery, ignoreCase = true))
                }



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color.White)
                ) {


                    /*CategoryFilterBar(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selected ->
                            selectedCategory = if (selectedCategory == selected) null else selected
                        }
                    )*/

                    Spacer(modifier = Modifier.height(8.dp))


                    ArticleCarousel(articles = articles,context = context)

                    Spacer(modifier = Modifier.height(8.dp))

                    SearchBar(
                        query = searchQuery,
                        onQueryChanged = { searchQuery = it }
                    )


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
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFFFF6B00))
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
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${article.price} €",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFFF6B00),
                            fontWeight = FontWeight.Bold
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
    onCategorySelected: (String) -> Unit,
    iconSize: Dp = 48.dp // par défaut inchangé
) {
    val categories = listOf(
        "women's clothing" to (Icons.Rounded.Female to "Femme"),
        "men's clothing" to (Icons.Default.Male to "Homme"),
        "jewelery" to (Icons.Default.Star to "Bijoux"),
        "electronics" to (Icons.Default.PhoneIphone to "Électronique")
    )

    val orange = Color(0xFFFF6B00)
    val orangeFaded = Color(0xFFFFA366)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { (key, pair) ->
            val (icon, label) = pair
            val isSelected = selectedCategory == key

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onCategorySelected(key) }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) orange else orangeFaded,
                    modifier = Modifier.size(iconSize)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) orange else orangeFaded,
                    fontSize = 12.sp
                )
            }
        }
    }
}



@Composable
fun ArticleCarousel(articles: List<Article>, context: Context) {
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
            .padding(vertical = 8.dp)
            ,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(articles.take(5)) { article ->
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(360.dp)
                    .clickable {
                        val intent = Intent(context, ArticleDetailsActivity::class.java).apply {
                            putExtra("article", article)
                        }
                        context.startActivity(intent)
                    },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFFFF6B00))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(article.image),
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${article.price} €",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF6B00),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CartButton(context: Context) {
    val cartItemsCount = remember { mutableStateOf(0) }
    
    // observe les changements du panier
    LaunchedEffect(Unit) {
        CartManager.getCurrentCart()?.let { cart ->
            cartItemsCount.value = cart.products.size
        }
    }

    Box {
        FloatingActionButton(
            onClick = {
                context.startActivity(Intent(context, CartActivity::class.java))
            },
            containerColor = Color(0xFFFF6B00),
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Panier"
            )
        }

        // affiche la bulle de notification si le panier n'est pas vide
        if (cartItemsCount.value > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = cartItemsCount.value.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Rechercher un article...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFF6B00),
            unfocusedBorderColor = Color.Gray
        )
    )
}



