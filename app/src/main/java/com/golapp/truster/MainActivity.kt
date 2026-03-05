package com.golapp.truster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.golapp.truster.functions.TrusterViewModel
import com.golapp.truster.ui.theme.TrusterTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm by remember { mutableStateOf(TrusterViewModel()) }
            val state = vm.state
            val debugAdding = remember { mutableStateOf(false) }
            TrusterTheme {
                Box(
                    Modifier
                        .background(Color.Gray.copy(alpha = .3f))
                        .padding(20.dp)
                        .systemBarsPadding()
                ) {
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
                                    Color.Black.copy(alpha = .5f),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            state.inventory.keys.forEach { item ->
                                val count = state.inventory[item] ?: 0
                                val text = buildString {
                                    append(item.title)
                                    when(val type = item.type) {
                                        ItemType.Gold -> {}
                                        is ItemType.Armor -> {}
                                        is ItemType.Food -> {}
                                        is ItemType.Potion -> {}
                                        is ItemType.Weapon -> {
                                            if (type.durability.current < type.durability.max) append(" (${type.durability})")
                                        }
                                    }
                                    if (count >1) append(" ($count)")
                                }
                                Text(text, modifier = Modifier.clickable {
                                    when(val type = item.type) {
                                        is ItemType.Armor -> {}
                                        is ItemType.Food -> {}
                                        ItemType.Gold -> {}
                                        is ItemType.Potion -> {}
                                        is ItemType.Weapon -> {
                                            val newItem = item.copy(type = type.copy(durability = type.durability.copy(
                                                current = type.durability.current-1
                                            )))
                                            vm.addItemToInventory(newItem, 1)
                                            vm.removeItemFromInventory(item, 1)
                                        }
                                    }
                                })
                            }
                        }


                        AnimatedVisibility(state.mainText.isNotEmpty()) {
                            Column() {
                                state.mainText.reversed().forEachIndexed { index, item ->
                                    if (index > 9) return@forEachIndexed
                                    val alpha = (6-index.toFloat())/10
                                    Text(item, color = Color.Black.copy(alpha = alpha))
                                }
                            }
                        }
                    }
                    Column(Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (debugAdding.value) {
                            listOf(Prefabs.Pistol.item, Prefabs.Bread.item, Prefabs.Water.item).forEach { item ->
                                Row {
                                    repeat(3) { time ->
                                        Button(
                                            onClick = { vm.addItemToInventory(item, time) },
                                            content = { Text("${item.title}+${time}") }
                                        )
                                    }
                                }
                            }
                            Button(
                                onClick = { vm.clearInventory() },
                                content = { Text("clear") }
                            )
                            Text("stackSize = ${state.mainText.size}")
                            Text("inventorySize = ${state.inventory.size}")
                        }
                        Row(
                            Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "debug adding"+if (debugAdding.value) " ^" else " v",
                                modifier = Modifier
                                    .alpha(if (debugAdding.value) .3f else 1f)
                                    .clickable { debugAdding.value = !debugAdding.value }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class InventoryItem(
    val title: String,
    val type: ItemType
)

data class Durability(
    val current: Int,
    val max: Int
) {
    override fun toString(): String {
        return "$current/$max"
    }
}

enum class FoodType {
    Eat, Drink
}

enum class BodyType {
    Head, Body,
    ArmLeft, ArmRight,
    LegLeft, LegRight
}

data class ArmorStat(
    val bodyType: BodyType,
    val defence: Map<BodyType, Int>
)

data class WeaponStat(
    val weaponType: WeaponType,
    val attack: Int
)

enum class WeaponType {
    MeleeOneHand, MeleeTwoHand,
    RangeOneHand, RangeTwoHand
}

sealed interface ItemType {
    data object Gold: ItemType
    data class Food(val restoreAmount: Int, val foodType: FoodType): ItemType
    data class Armor(val durability: Durability, val stat: ArmorStat): ItemType
    data class Weapon(val durability: Durability, val stat: WeaponStat): ItemType
    data class Potion(val durability: Durability, val defence: Int): ItemType
}


