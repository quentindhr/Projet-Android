package fr.epf.min2.projet_kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import fr.epf.min2.projet_kotlin.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.platform.ComposeView
import android.view.ViewGroup
import android.widget.FrameLayout
import fr.epf.min2.projet_kotlin.components.CartButton
import fr.epf.min2.projet_kotlin.components.BackButton // Import the new BackButton composable

class ArticleDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        // récupère l'article passé
        val article = intent.getSerializableExtra("article", Article::class.java)

        // vérifie si l'article existe
        if (article == null) {
            Toast.makeText(this, "Erreur : article non trouvé", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ajoute le bouton flottant du panier (top right)
        val cartComposeView = ComposeView(this).apply {
            setContent {
                Row {
                    CartButton(this@ArticleDetailsActivity)
                }
            }
        }

        // crée les paramètres de mise en page pour positionner le bouton du panier en haut à droite
        val cartLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.TOP or android.view.Gravity.END
            topMargin = 115
            rightMargin = 16
        }
        addContentView(cartComposeView, cartLayoutParams)

        // --- Add the Back Button (top left) ---
        val backComposeView = ComposeView(this).apply {
            setContent {
                Row {
                    BackButton(this@ArticleDetailsActivity) {
                        onBackPressedDispatcher.onBackPressed() // Handle back press
                    }
                }
            }
        }


        val backLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            topMargin = 115
            leftMargin = 16
        }
        addContentView(backComposeView, backLayoutParams)


        findViewById<TextView>(R.id.titleTextView).text = article.title
        findViewById<TextView>(R.id.priceTextView).text = "${article.price} €"
        findViewById<TextView>(R.id.categoryTextView).text = article.category
        findViewById<TextView>(R.id.descriptionTextView).text = article.description

        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(imageView.context)
            .load(article.image)
            .into(imageView)

        // initialise le panier
        CoroutineScope(Dispatchers.Main).launch {
            CartManager.initializeCart()
        }

        findViewById<Button>(R.id.addToCartButton).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    CartManager.addToCart(article)
                    Toast.makeText(this@ArticleDetailsActivity, "Ajouté au panier", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@ArticleDetailsActivity, "Erreur lors de l'ajout au panier", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}