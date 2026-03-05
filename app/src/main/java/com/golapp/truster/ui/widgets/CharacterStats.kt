package com.golapp.truster.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golapp.truster.data.CharacterData

@Composable
fun CharacterStats(stats: CharacterData) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        listOf(stats.health, stats.stamina, stats.hunger, stats.trist, stats.exp).forEach { stat ->
            Row() {
                CustomText(stat.title, TextStyle(fontSize = 8.sp), modifier = Modifier.width(50.dp))
                Box(Modifier.height(10.dp).fillMaxWidth(.5f)) {
                    Box(Modifier.align(Alignment.CenterStart).fillMaxHeight().fillMaxWidth(stat.value.current.toFloat()/stat.value.max).background(stat.color))
                    CustomText(stat.value.toString(), TextStyle(fontSize = 8.sp), modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}