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
    private val cartManager: CartManager
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

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
        val article = articles[position]
        val quantity = articles.count { it.id == article.id }

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
                cartManager.removeFromCart(article)
                updateCart()
            }
        }
    }

    override fun getItemCount() = articles.size

    private fun updateCart() {
        cartManager.getCurrentCart()?.let { cart ->
            articles = cart.products
            notifyDataSetChanged()
        }
    }

    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
} 