package fr.epf.min2.projet_kotlin

import retrofit2.http.*
import fr.epf.min2.projet_kotlin.model.Article
import fr.epf.min2.projet_kotlin.model.Cart

interface ApiService {
    // Récupère tous les articles
    @GET("products")
    suspend fun getAllArticles(): List<Article>

    // Récupère un article par son ID
    @GET("products/{id}")
    suspend fun getArticleById(@Path("id") id: Int): Article

    // Récupère un panier spécifique
    @GET("carts/{id}")
    suspend fun getCart(@Path("id") id: Int): Cart

    // Crée un nouveau panier
    @POST("carts")
    suspend fun createCart(@Body cart: Cart): Cart

    // Met à jour un panier existant
    @PUT("carts/{id}")
    suspend fun updateCart(@Path("id") id: Int, @Body cart: Cart): Cart

    // Supprime un panier
    @DELETE("carts/{id}")
    suspend fun deleteCart(@Path("id") id: Int)
}