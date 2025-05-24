package fr.epf.min2.projet_kotlin

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import fr.epf.min2.projet_kotlin.model.Article

class ArticleDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        // récupère l'article passé
        val article = intent.getParcelableExtra("article", Article::class.java)
        article?.let { displayArticle(it) }
    }

    private fun displayArticle(article: Article) {
        findViewById<TextView>(R.id.titleTextView).text = article.title
        findViewById<TextView>(R.id.priceTextView).text = "Prix: ${article.price}€"
        findViewById<TextView>(R.id.categoryTextView).text = "Catégorie: ${article.category}"
        findViewById<TextView>(R.id.descriptionTextView).text = article.description
    }
} 