package fr.epf.min2.projet_kotlin

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var validateButton: MaterialButton
    private lateinit var emptyCartText: TextView
    private lateinit var totalPriceText: TextView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // configuration du callback pour le bouton retour
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        recyclerView = findViewById(R.id.cartRecyclerView)
        validateButton = findViewById(R.id.validateButton)
        emptyCartText = findViewById(R.id.emptyCartText)
        totalPriceText = findViewById(R.id.totalText)

        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(emptyList(), CartManager) { total ->
            totalPriceText.text = String.format("%.2f €", total)
        }
        recyclerView.adapter = cartAdapter

        validateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    CartManager.getCurrentCart()?.let { cart ->
                        if (cart.products.isNotEmpty()) {
                            CartManager.validateOrder()
                            Toast.makeText(this@CartActivity, "Commande validée !", Toast.LENGTH_SHORT).show()
                            updateCartDisplay()
                            finish()
                        } else {
                            Toast.makeText(this@CartActivity, "Le panier est vide", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CartActivity, "Erreur lors de la validation", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // initialise et charge le panier
        CoroutineScope(Dispatchers.Main).launch {
            CartManager.initializeCart()
            updateCartDisplay()
        }
    }

    private fun updateCartDisplay() {
        CartManager.getCurrentCart()?.let { cart ->
            if (cart.products.isEmpty()) {
                recyclerView.visibility = RecyclerView.GONE
                emptyCartText.visibility = TextView.VISIBLE
            } else {
                recyclerView.visibility = RecyclerView.VISIBLE
                emptyCartText.visibility = TextView.GONE
                cartAdapter.updateArticles(cart.products)
            }
        }
    }
} 