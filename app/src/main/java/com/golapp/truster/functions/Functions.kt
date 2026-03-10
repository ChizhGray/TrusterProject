package com.golapp.truster.functions

import com.golapp.truster.data.InventoryItem

fun getItemText(item: InventoryItem, count: Int): String {
    return buildString {
        append(item.title)
        if (count >1) append(" ($count)")
    }
}