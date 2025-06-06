package fr.epf.min2.projet_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import fr.epf.min2.projet_kotlin.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(
    private var articles: List<Article>,
    private val cartManager: CartManager,
    private val onTotalUpdated: (Double) -> Unit,
    private val onArticleClick: (Article) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // classe pour représenter un article groupé avec sa quantité
    data class GroupedArticle(
        val article: Article,
        val quantity: Int
    )

    // liste des articles groupés
    private var groupedArticles: List<GroupedArticle> = emptyList()

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage = view.findViewById<ImageView>(R.id.productImage)
        val productNameText = view.findViewById<TextView>(R.id.productNameText)
        val productPriceText = view.findViewById<TextView>(R.id.productPriceText)
        val quantityText = view.findViewById<TextView>(R.id.quantityText)
        val increaseButton = view.findViewById<MaterialButton>(R.id.increaseButton)
        val decreaseButton = view.findViewById<MaterialButton>(R.id.decreaseButton)
        val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val groupedArticle = groupedArticles[position]
        val article = groupedArticle.article
        val quantity = groupedArticle.quantity

        holder.itemView.setOnClickListener {
            onArticleClick(article)
        }

        holder.productNameText.text = article.title
        holder.productPriceText.text = "${article.price} €"
        holder.quantityText.text = quantity.toString()

        Glide.with(holder.itemView.context)
            .load(article.image)
            .into(holder.productImage)

        holder.increaseButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                cartManager.updateQuantity(article, quantity + 1)
                updateCart()
            }
        }

        holder.decreaseButton.setOnClickListener {
            if (quantity > 1) {
                CoroutineScope(Dispatchers.Main).launch {
                    cartManager.updateQuantity(article, quantity - 1)
                    updateCart()
                }
            }
        }

        holder.deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                cartManager.updateQuantity(article, 0)
                updateCart()
            }
        }
    }

    override fun getItemCount() = groupedArticles.size

    private fun updateCart() {
        cartManager.getCurrentCart()?.let { cart ->
            articles = cart.products
            updateGroupedArticles()
            notifyDataSetChanged()
        }
    }

    private fun updateGroupedArticles() {
        // map pour stocker les quantités
        val quantities = mutableMapOf<Int, Int>()
        articles.forEach { article ->
            quantities[article.id] = (quantities[article.id] ?: 0) + 1
        }
        
        // liste groupée
        groupedArticles = articles.distinctBy { it.id }
            .map { article ->
                GroupedArticle(
                    article = article,
                    quantity = quantities[article.id] ?: 0
                )
            }
        
        // calcul de la somme totale
        val total = groupedArticles.sumOf { it.article.price * it.quantity }
        onTotalUpdated(total)
    }

    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        updateGroupedArticles()
        notifyDataSetChanged()
    }
} 