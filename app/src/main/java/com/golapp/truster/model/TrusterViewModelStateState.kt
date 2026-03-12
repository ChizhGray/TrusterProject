package com.golapp.truster.model

import androidx.compose.ui.graphics.Color
import com.golapp.truster.data.CharacterData
import com.golapp.truster.data.CharacterStat
import com.golapp.truster.data.Durability
import com.golapp.truster.data.Enemy
import com.golapp.truster.data.InventoryItem

data class TrusterViewModelStateState(
    val gameRunning: Boolean,
    val character: CharacterData,
    val inventory: Map<InventoryItem, Int>,
    val mainText: List<String>,
    val enemy: Enemy?
) {
    fun getInventoryTexted(): String {
        return if (inventory.isEmpty()) "inventory is empty"
        else "items: ${inventory.toList().joinToString { "${it.first.title} (${it.second})" }}"
    }
    companion object {
        val initialState = TrusterViewModelStateState(
            gameRunning = true,
            inventory = mapOf(),
            mainText = listOf(),
            character = CharacterData(
                health = CharacterStat("health", Color.Green.copy(alpha = .8f),Durability(100, 100)),
                stamina = CharacterStat("stamina", Color.Yellow.copy(alpha = .8f),Durability(100, 100)),
                hunger = CharacterStat("hunger", Color.Green.copy(alpha = .3f),Durability(100, 100)),
                trist = CharacterStat("trist", Color.Blue.copy(alpha = .5f),Durability(100, 100)),
                exp = CharacterStat("exp", Color.Gray.copy(alpha = .8f),Durability(0, 1000)),
                equippedItem = null
            ),
            enemy = null
        )
    }
}