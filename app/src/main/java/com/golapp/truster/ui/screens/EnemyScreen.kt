package com.golapp.truster.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.golapp.truster.data.Enemy
import com.golapp.truster.ui.widgets.ProgressBarItem

@Composable
fun EnemyScreen(enemy: Enemy?) {
    enemy?.let {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(enemy.name)
            ProgressBarItem(enemy.hp, Color.Red.copy(alpha = .5f), Modifier.fillMaxWidth(.3f))
        }
    }
}