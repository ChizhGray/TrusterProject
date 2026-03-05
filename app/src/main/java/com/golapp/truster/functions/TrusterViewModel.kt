package com.golapp.truster.functions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golapp.truster.InventoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrusterViewModel: ViewModel() {
    private var _container: MutableState<TrusterViewModelStateState> = mutableStateOf(TrusterViewModelStateState.initialState)
    val state get() = _container.value

    private fun reduce(fields: (TrusterViewModelStateState) -> TrusterViewModelStateState) { _container.value = fields(_container.value) }
    fun showMessage(text: String) {
        reduce { it.copy(mainText = it.mainText.plus(text)) }
        viewModelScope.launch {
            delay(1000)
            removeFirstMessage()
        }
    }

    fun removeFirstMessage() {
        val newList = state.mainText.toMutableList().ifEmpty { return }
        newList.removeFirst()
        reduce { it.copy(mainText = newList) }
    }

    fun addItemToInventory(item: InventoryItem, count: Int) {
        if (count<=0) { showMessage("count<=0"); return }
        if (state.inventory.keys.contains(item)) {
            val newInventory = state.inventory.mapValues { (inventoryItem, inventoryItemCount) ->
                if (inventoryItem==item) inventoryItemCount + count
                else inventoryItemCount
            }
            reduce { it.copy(inventory = newInventory) }
        } else {
            val newInventory = state.inventory.plus(item to count)
            reduce { it.copy(inventory = newInventory) }
        }
        showMessage("added ${item.title} ($count)")
        Log.i("addItemToInventory", state.getInventoryTexted())
    }

    fun removeItemFromInventory(item: InventoryItem, count: Int) {
        if (count<=0) { showMessage("count<=0"); return }
        if (state.inventory.keys.contains(item)) {
            val itemCount = state.inventory[item]!!
            if (itemCount>1) {
                val newInventory = state.inventory.mapValues { (inventoryItem, inventoryItemCount) ->
                    if (inventoryItem==item) inventoryItemCount - count
                    else inventoryItemCount
                }
                reduce { it.copy(inventory = newInventory) }
                showMessage("removed 1 ${item.title}")
            } else {
                val newInventory = state.inventory.minus(item)
                reduce { it.copy(inventory = newInventory) }
                showMessage("removed last ${item.title}")
            }
        } else showMessage("no items!")
        Log.i("removeItemFromInventory", state.getInventoryTexted())
    }

    fun clearInventory() {
        state.inventory.ifEmpty { return }
        reduce { it.copy(inventory = emptyMap()) }
        Log.i("clearInventory", state.getInventoryTexted())
    }
}