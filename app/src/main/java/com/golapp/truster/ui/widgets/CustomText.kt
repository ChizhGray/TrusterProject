package com.golapp.truster.ui.widgets

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.golapp.truster.ui.theme.PrimaryTextL

@Composable
fun CustomText(text: String, textStyle: TextStyle? = null, modifier: Modifier = Modifier) {
    Text(text = text, style = textStyle ?: LocalTextStyle.current, modifier = modifier, color = PrimaryTextL)
}