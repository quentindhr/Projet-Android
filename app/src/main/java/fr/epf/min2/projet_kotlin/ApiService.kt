package fr.epf.min2.projet_kotlin

import retrofit2.http.GET
import retrofit2.http.Path
import fr.epf.min2.projet_kotlin.Article

interface ApiService {
    // Récupère tous les articles
    @GET("products")
    suspend fun getAllArticles(): List<Article>

    // Récupère un article par son ID
    @GET("products/{id}")
    suspend fun getArticleById(@Path("id") id: Int): Article
}