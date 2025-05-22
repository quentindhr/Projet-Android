package fr.epf.min2.projet_kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter()
        recyclerView.adapter = adapter

        loadCart()
    }

    private fun loadCart() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cart = ApiClient.api.getCart(1) // panier avec id 1
                withContext(Dispatchers.Main) {
                    adapter.submitList(cart.products)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CartActivity, "Erreur de chargement du panier", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
} 