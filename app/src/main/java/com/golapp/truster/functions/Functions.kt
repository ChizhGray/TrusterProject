package com.golapp.truster.functions

import com.golapp.truster.data.InventoryItem
import com.golapp.truster.data.ItemType

fun getItemText(item: InventoryItem, count: Int): String {
    return buildString {
        append(item.title)
        when(val type = item.type) {
            ItemType.Gold -> {}
            is ItemType.Armor -> {}
            is ItemType.Food -> {}
            is ItemType.Potion -> {}
            is ItemType.Weapon -> {
                if (type.durability.current < type.durability.max) append(" (${type.durability})")
            }
        }
        if (count >1) append(" ($count)")
    }
}