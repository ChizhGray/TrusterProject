package com.golapp.truster.functions

import android.util.Log
import com.golapp.truster.data.InventoryItem

fun getItemText(item: InventoryItem, count: Int): String {
    return buildString {
        append(item.title)
        if (count >1) append(" ($count)")
    }
}

fun getChance(percent: Int): Boolean {
    val randomNumber = (Math.random()*100).toInt()
    Log.i("test-random", "percent=$percent, random=$randomNumber")
    return percent>=randomNumber
}