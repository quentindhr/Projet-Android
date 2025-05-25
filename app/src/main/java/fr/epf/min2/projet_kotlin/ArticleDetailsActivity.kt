package fr.epf.min2.projet_kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import fr.epf.min2.projet_kotlin.model.Article

class ArticleDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

// récupère l'article passé
        val article = intent.getSerializableExtra("article") as Article

        findViewById<TextView>(R.id.titleTextView).text = article.title
        findViewById<TextView>(R.id.priceTextView).text = "${article.price} €"
        findViewById<TextView>(R.id.categoryTextView).text = article.category
        findViewById<TextView>(R.id.descriptionTextView).text = article.description

        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this)
            .load(article.image)
            .into(imageView)

        findViewById<Button>(R.id.addToCartButton).setOnClickListener {
            // TODO: Ajouter au panier (tu peux gérer ça via SharedPreferences, ViewModel, DB ou autre)
            Toast.makeText(this, "${article.title} ajouté au panier", Toast.LENGTH_SHORT).show()
        }





    }


}
