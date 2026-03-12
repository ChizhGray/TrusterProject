package com.golapp.truster.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golapp.truster.data.CharacterData

@Composable
fun CharacterStats(stats: CharacterData, unequipWeapon: () -> Unit) {
    Row() {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            listOf(stats.health, stats.stamina, stats.hunger, stats.trist, stats.exp).forEach { stat ->
                Row {
                    CustomText(stat.title, TextStyle(fontSize = 8.sp), modifier = Modifier.width(50.dp))
                    ProgressBarItem(stat.value, stat.color, Modifier.fillMaxWidth(.5f))
                }
            }
        }
        stats.equippedItem?.let {
            Column {
                CustomText("equipped: ${it.title}")
                CustomButton("unequip") { unequipWeapon() }
            }
        }
    }
}