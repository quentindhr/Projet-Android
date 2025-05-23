package fr.epf.min2.projet_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var validateButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        try {
            recyclerView = findViewById(R.id.cartRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = CartAdapter()
            recyclerView.adapter = adapter

            validateButton = findViewById(R.id.validateButton)
            validateButton.setOnClickListener {
                Toast.makeText(this, "Commande confirmée, merci !", Toast.LENGTH_SHORT).show()
                finish()
            }

            loadCart()
        } catch (e: Exception) {
            Log.e("CartActivity", "Erreur lors de l'initialisation: ${e.message}", e)
            Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadCart() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("CartActivity", "Chargement du panier...")
                val cart = ApiClient.api.getCart(1)
                Log.d("CartActivity", "Panier reçu: $cart")
                
                withContext(Dispatchers.Main) {
                    try {
                        adapter.submitList(cart.products)
                        Log.d("CartActivity", "Liste mise à jour avec ${cart.products.size} produits")
                    } catch (e: Exception) {
                        Log.e("CartActivity", "Erreur lors de la mise à jour de la liste: ${e.message}", e)
                        Toast.makeText(this@CartActivity, "Erreur d'affichage des produits", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("CartActivity", "Erreur lors du chargement du panier: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CartActivity, "Erreur de chargement du panier", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
} 