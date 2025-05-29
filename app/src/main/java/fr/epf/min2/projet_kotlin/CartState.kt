package fr.epf.min2.projet_kotlin

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CartState {
    private val _cartItemsCount = MutableStateFlow(0)
    val cartItemsCount: StateFlow<Int> = _cartItemsCount.asStateFlow()

    fun updateCartCount(count: Int) {
        _cartItemsCount.value = count
    }
} 