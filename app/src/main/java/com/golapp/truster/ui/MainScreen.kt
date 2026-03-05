package com.golapp.truster.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.golapp.truster.functions.getItemText
import com.golapp.truster.model.TrusterViewModel
import com.golapp.truster.ui.widgets.CustomText

@Composable
fun MainScreen(vm: TrusterViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            Modifier
                .height(200.dp)
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.Black.copy(alpha = .8f),
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.Start
        ) {
            vm.state.inventory.keys.sortedBy { it.title }.forEach { item ->
                val count = vm.state.inventory[item] ?: 0
                val text = getItemText(item, count)
                CustomText(text, modifier = Modifier.clickable {
                    vm.useItem(item)
                })
            }
        }


        AnimatedVisibility(vm.state.mainText.isNotEmpty()) {
            Column() {
                vm.state.mainText.reversed().forEachIndexed { index, item ->
                    if (index > 9) return@forEachIndexed
                    val alpha = (6-index.toFloat())/10
                    CustomText(item, modifier = Modifier.alpha(alpha))
                }
            }
        }
    }
}