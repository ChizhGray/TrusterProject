package com.golapp.truster.functions

import com.golapp.truster.InventoryItem

data class TrusterViewModelStateState(
    val inventory: Map<InventoryItem, Int>,
    val mainText: List<String>
) {
    fun getInventoryTexted(): String {
        return if (inventory.isEmpty()) "inventory is empty"
        else "items: ${inventory.toList().joinToString { "${it.first.title} (${it.second})" }}"
    }
    companion object {
        val initialState = TrusterViewModelStateState(
            inventory = mapOf(),
            mainText = listOf()
        )
    }
}