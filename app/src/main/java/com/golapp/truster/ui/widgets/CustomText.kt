package com.golapp.truster.ui.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.golapp.truster.ui.theme.PrimaryTextL

@Composable
fun CustomText(text: String, modifier: Modifier = Modifier) {
    Text(text = text, modifier = modifier, color = PrimaryTextL)
}