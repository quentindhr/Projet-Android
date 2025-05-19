package fr.epf.min2.projet_kotlin

data class Article(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)
