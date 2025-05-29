package fr.epf.min2.projet_kotlin.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.epf.min2.projet_kotlin.CartActivity
import fr.epf.min2.projet_kotlin.CartState

@Composable
fun CartButton(context: Context) {
    // observe le compteur du panier
    val cartItemsCount by CartState.cartItemsCount.collectAsState()

    Box(
        modifier = Modifier.size(72.dp) // taille plus grande pour permettre le débordement
    ) {
        FloatingActionButton(
            onClick = {
                context.startActivity(Intent(context, CartActivity::class.java))
            },
            containerColor = Color(0xFFFF6B00),
            contentColor = Color.White,
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Panier"
            )
        }

        // affiche la bulle de notification si le panier n'est pas vide
        if (cartItemsCount > 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    text = cartItemsCount.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
} 