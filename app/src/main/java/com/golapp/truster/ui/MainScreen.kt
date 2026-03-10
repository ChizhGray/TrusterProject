package com.golapp.truster.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.golapp.truster.data.InventoryItem
import com.golapp.truster.data.ItemType
import com.golapp.truster.model.TrusterViewModel
import com.golapp.truster.ui.widgets.CustomText
import com.golapp.truster.ui.widgets.ProgressBarItem

@Composable
fun MainScreen(vm: TrusterViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val cellsCount = 6
        val itemGroups = remember(vm.state.inventory) { vm.state.inventory.keys.groupBy { group ->
            when(group.type) {
                is ItemType.Armor -> group.title
                is ItemType.Food -> group.title
                ItemType.Gold -> group.title
                is ItemType.Potion -> group.title
                is ItemType.Weapon -> group.title
            }
        }.map { gr -> gr.key } }
        LazyVerticalGrid(
            columns = GridCells.Fixed(cellsCount),
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.Black.copy(alpha = .8f),
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            items(itemGroups) { item ->
                val dropDownState = remember { mutableStateOf(false) }
                val filtered = vm.state.inventory.filter { it.key.title == item }
                val sum = filtered.values.sum()
                Column(
                    Modifier
                        .padding(2.dp)
                        .height(40.dp)
                        .fillMaxWidth(1/cellsCount.toFloat())
                        .border(1.dp, Color.Black, RoundedCornerShape(2.dp))
                        .clickable { dropDownState.value = true }
                        .padding(2.dp)
                ) {
                    CustomText("$item ($sum)", textStyle = TextStyle(fontSize = 10.sp))
                    DropdownMenu(dropDownState.value, { dropDownState.value = false }, containerColor = Color.Gray) {
                        Column(Modifier.padding(5.dp).width(100.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            filtered.keys.firstOrNull()?.let {
                                CustomText(it.description, textStyle = TextStyle(fontSize = 8.sp))
                            }
                            filtered.forEach { group ->
                                val item = group.key
                                repeat(group.value) {
                                    when(val type = item.type) {
                                        is ItemType.Weapon -> ProgressBarItem(type.stat.durability, Color.Green,
                                            Modifier.fillMaxWidth().clickable { vm.useItem(item); dropDownState.value = false })
                                        else -> { CustomText("use", modifier =
                                            Modifier.fillMaxWidth().clickable { vm.useItem(item); dropDownState.value = false })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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