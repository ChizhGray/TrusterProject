package com.golapp.truster.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.golapp.truster.data.Durability
import com.golapp.truster.data.Enemy
import com.golapp.truster.data.ItemsPrefabs
import com.golapp.truster.model.TrusterViewModel
import com.golapp.truster.ui.widgets.CustomButton
import com.golapp.truster.ui.widgets.CustomText

@Composable
fun DebugScreen(vm: TrusterViewModel) {
    Column(
        Modifier.fillMaxWidth().navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row() {
            LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxWidth(.5f)) {
                items(ItemsPrefabs.entries) { prefab ->
                    CustomButton("${prefab.item.title}+1", modifier = Modifier.padding(1.dp), color = Color.Green.copy(alpha = .2f)) {
                        vm.addItemToInventory(prefab.item, 1)
                    }
                }
            }
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                CustomButton("clear inventory", color = Color.Red.copy(alpha = .4f)) { vm.clearInventory() }
                CustomButton("remove enemy", color = Color.Red.copy(alpha = .2f)) { vm.editEnemy(null) }
                CustomButton("add enemy", color = Color.Green.copy(alpha = .3f)) {
                    vm.editEnemy(
                        Enemy(
                            "random enemy",
                            Durability.set(100),
                            7,
                            25
                        )
                    )
                }
            }
        }
        CustomText("stackSize = ${vm.state.mainText.size}")
        CustomText("inventorySize = ${vm.state.inventory.size}")
    }

}