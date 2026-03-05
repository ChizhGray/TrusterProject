package com.golapp.truster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.golapp.truster.data.Prefabs
import com.golapp.truster.model.TrusterViewModel
import com.golapp.truster.ui.MainScreen
import com.golapp.truster.ui.theme.TrusterTheme
import com.golapp.truster.ui.widgets.CharacterStats
import com.golapp.truster.ui.widgets.CustomButton
import com.golapp.truster.ui.widgets.CustomText
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = TrusterViewModel()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val debugSheetState = rememberBottomSheetScaffoldState()
            val uiScope = rememberCoroutineScope()
            TrusterTheme {
                BottomSheetScaffold(
                    contentColor = Color.Unspecified,
                    containerColor = Color.Unspecified,
                    sheetContentColor = Color.Unspecified,
                    sheetContainerColor = Color.Gray,
                    sheetPeekHeight = 0.dp,
                    content = {
                        Box(
                            Modifier
                                .padding(20.dp)
                                .systemBarsPadding()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                CharacterStats(stats = vm.state.character)
                                MainScreen(vm = vm)
                            }
                            CustomText(
                                text = "debug adding",
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                                        uiScope.launch { debugSheetState.bottomSheetState.expand() }
                                    }
                            )
                        }
                    },
                    scaffoldState = debugSheetState,
                    sheetContent = {
                        Column(
                            modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Prefabs.entries.forEach { prefab ->
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    repeat(3) { time ->
                                        CustomButton("${prefab.item.title}+${time}", modifier = Modifier.width(80.dp)) {
                                            vm.addItemToInventory(prefab.item, time)
                                        }
                                    }
                                }
                            }
                            CustomButton("clear") { vm.clearInventory() }
                            CustomText("stackSize = ${vm.state.mainText.size}")
                            CustomText("inventorySize = ${vm.state.inventory.size}")
                        }
                    },
                )
            }
        }
    }
}


