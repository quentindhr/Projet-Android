package fr.epf.min2.projet_kotlin.model

data class Cart(
    val id: Int,
    val userId: Int,
    val products: List<Article>
)