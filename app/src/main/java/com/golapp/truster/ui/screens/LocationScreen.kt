package com.golapp.truster.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LocationScreen(
    enemyContent: @Composable () -> Unit,
    actionsContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxWidth().height(200.dp)
    ) {
        Column(Modifier.align(Alignment.TopCenter)) { enemyContent() }
        Column(Modifier.align(Alignment.BottomCenter)) { actionsContent() }
    }
}