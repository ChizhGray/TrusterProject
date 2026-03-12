package com.golapp.truster.data

import androidx.compose.ui.graphics.Color


data class CharacterData(
    val health: CharacterStat,
    val stamina: CharacterStat,
    val trist: CharacterStat,
    val hunger: CharacterStat,
    val exp: CharacterStat,
    val equippedItem: InventoryItem?
)

data class CharacterStat(
    val title: String,
    val color: Color,
    val value: Durability
)