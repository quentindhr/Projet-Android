package fr.epf.min2.projet_kotlin

import fr.epf.min2.projet_kotlin.model.Article
import fr.epf.min2.projet_kotlin.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartManager {
    private var currentCart: Cart? = null
    private val api = ApiClient.api

    // initialise le panier
    suspend fun initializeCart() {
        try {
            // on crée un nouveau panier si on n'en a pas
            if (currentCart == null) {
                currentCart = api.createCart(Cart(id=0, userId=0, products=emptyList()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // récupère le panier actuel
    fun getCurrentCart(): Cart? = currentCart

    // ajoute un article au panier
    suspend fun addToCart(article: Article) {
        withContext(Dispatchers.IO) {
            try {
                currentCart?.let { cart ->
                    val updatedProducts = cart.products.toMutableList().apply {
                        add(article)
                    }
                    val updatedCart = cart.copy(products = updatedProducts)
                    currentCart = api.updateCart(cart.id, updatedCart)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // supprime un article du panier
    suspend fun removeFromCart(article: Article) {
        withContext(Dispatchers.IO) {
            try {
                currentCart?.let { cart ->
                    val updatedProducts = cart.products.toMutableList().apply {
                        remove(article)
                    }
                    val updatedCart = cart.copy(products = updatedProducts)
                    currentCart = api.updateCart(cart.id, updatedCart)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // met à jour la quantité d'un article
    suspend fun updateQuantity(article: Article, quantity: Int) {
        withContext(Dispatchers.IO) {
            try {
                currentCart?.let { cart ->
                    val updatedProducts = cart.products.toMutableList().apply {
                        removeAll { it.id == article.id }
                        repeat(quantity) { add(article) }
                    }
                    val updatedCart = cart.copy(products = updatedProducts)
                    currentCart = api.updateCart(cart.id, updatedCart)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    // vide le panier
    suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            try {
                currentCart?.let { cart ->
                    val updatedCart = cart.copy(products = emptyList())
                    currentCart = api.updateCart(cart.id, updatedCart)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
} 