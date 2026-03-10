package com.golapp.truster.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golapp.truster.data.Durability

@Composable
fun ProgressBarItem(value: Durability, color: Color, modifier: Modifier = Modifier) {
    Box(modifier.height(10.dp).background(Color.Gray.copy(alpha = .3f))) {
        Box(Modifier.align(Alignment.CenterStart).fillMaxHeight().fillMaxWidth(value.current.toFloat()/value.max).background(color))
        CustomText(value.toString(), TextStyle(fontSize = 8.sp), modifier = Modifier.align(Alignment.Center))
    }
}