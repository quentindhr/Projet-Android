package fr.epf.min2.projet_kotlin.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context
import androidx.compose.material3.ButtonDefaults // Import ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun BackButton(context: Context, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B00)), // Set the background color
        modifier = Modifier.padding(16.dp) // Adjust padding as needed
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Retour"
        )
    }
}