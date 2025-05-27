package fr.epf.min2.projet_kotlin

import fr.epf.min2.projet_kotlin.model.Article
import fr.epf.min2.projet_kotlin.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CartManager {
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

    // récupère la quantité d'un article dans le panier
    private fun getArticleQuantity(articleId: Int): Int {
        return currentCart?.products?.count { it.id == articleId } ?: 0
    }

    // ajoute un article au panier
    suspend fun addToCart(article: Article) {
        withContext(Dispatchers.IO) {
            try {
                currentCart?.let { cart ->
                    val updatedProducts = cart.products.toMutableList().apply {
                        // on ajoute simplement l'article
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
                        // on supprime la première occurrence de l'article
                        val index = indexOfFirst { it.id == article.id }
                        if (index != -1) {
                            removeAt(index)
                        }
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
                    val updatedProducts = cart.products.toMutableList()
                    // on trouve l'index du premier article avec cet id
                    val firstIndex = updatedProducts.indexOfFirst { it.id == article.id }
                    if (firstIndex != -1) {
                        // on supprime tous les articles avec cet id
                        updatedProducts.removeAll { it.id == article.id }
                        // on insère les nouveaux articles à la même position
                        repeat(quantity) { index ->
                            updatedProducts.add(firstIndex + index, article)
                        }
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

    // valide la commande et vide le panier
    suspend fun validateOrder() {
        try {
            clearCart()
            // on réinitialise le panier
            currentCart = api.createCart(Cart(id=0, userId=0, products=emptyList()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 