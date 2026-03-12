package com.golapp.truster.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golapp.truster.data.ItemType
import com.golapp.truster.model.TrusterViewModel
import com.golapp.truster.ui.widgets.CustomButton
import com.golapp.truster.ui.widgets.CustomText
import com.golapp.truster.ui.widgets.ProgressBarItem

@Composable
fun InventoryScreen(vm: TrusterViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
            items(itemGroups.sortedBy { it }) { item ->
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
                    DropdownMenu(dropDownState.value, modifier = Modifier.fillMaxWidth(.5f), onDismissRequest = { dropDownState.value = false }, containerColor = Color.Gray) {
                        Column(Modifier.padding(5.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            filtered.keys.firstOrNull()?.let {
                                CustomText(it.description, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(fontSize = 8.sp))
                            }
                            filtered.toSortedMap(comparator = compareBy {
                                when(it.type) {
                                    is ItemType.Armor -> it.type.stat.durability.current
                                    is ItemType.Food -> it.title
                                    ItemType.Gold -> it.title
                                    is ItemType.Potion -> it.type.stat.potionType.name
                                    is ItemType.Weapon -> it.type.stat.durability.current
                                }
                            }).forEach { group ->
                                val item = group.key
                                repeat(group.value) {
                                    when(val type = item.type) {
                                        is ItemType.Weapon -> {
                                            Box() {
                                                Column {
                                                    ProgressBarItem(
                                                        type.stat.durability,
                                                        Color.Green,
                                                        Modifier.fillMaxWidth()
                                                    )
                                                    Row {
                                                        CustomButton("remove", modifier = Modifier.fillMaxWidth(.5f)) {
                                                            vm.removeItemFromInventory(item, 1)
                                                        }
                                                        CustomButton("equip", modifier = Modifier.fillMaxWidth()) {
                                                            vm.equipWeapon(item)
                                                            dropDownState.value = false
                                                        }
                                                    }
                                                }
                                                if (item == vm.state.character.equippedItem) {
                                                    Box(Modifier.size(5.dp).clip(CircleShape).background(Color.Red).align(
                                                        Alignment.TopEnd))
                                                }
                                            }

                                        }
                                        else -> {
                                            Row() {
                                                CustomButton("remove", modifier = Modifier.fillMaxWidth(.5f)) {
                                                    vm.removeItemFromInventory(item, 1)
                                                }
                                                CustomButton("use", modifier = Modifier.fillMaxWidth()) {
                                                    vm.useItem(item)
                                                    dropDownState.value = false
                                                }
                                            }
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