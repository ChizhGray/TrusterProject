package com.golapp.truster.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golapp.truster.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrusterViewModel: ViewModel() {
    private var _container: MutableState<TrusterViewModelStateState> = mutableStateOf(TrusterViewModelStateState.initialState)
    val state get() = _container.value

    private fun reduce(fields: (TrusterViewModelStateState) -> TrusterViewModelStateState) { _container.value = fields(_container.value) }
    fun showMessage(text: String) {
        reduce { it.copy(mainText = it.mainText.plus(text)) }
        viewModelScope.launch {
            delay(3000)
            removeFirstMessage()
        }
    }
    init {
        Prefabs.entries.forEach { prefab ->
            addItemToInventory(prefab.item, 1)
        }
    }

    fun statTick(trist: Int, hunger: Int) {
        val calcTrist = state.character.trist.value.current-trist
        val tristToReduce = if (calcTrist <= 0) 0 else calcTrist

        val calcHunger = state.character.hunger.value.current-hunger
        val hungerToReduce = if (calcHunger <= 0) 0 else calcHunger

        reduce {
            it.copy(
                character = it.character.copy(
                    trist = it.character.trist.copy(value = it.character.trist.value.copy(current = tristToReduce)),
                    hunger = it.character.hunger.copy(value = it.character.hunger.value.copy(current = hungerToReduce))
                )
            )
        }

        val healthByHungerNew = if (hungerToReduce == 0) 2 else 0
        val healthByTristNew = if (tristToReduce == 0) 5 else 0
        val healthToReduceTotal = healthByTristNew + healthByHungerNew

        if (healthToReduceTotal!=0) {
            reduce {
                it.copy(
                    character = it.character.copy(
                        health = it.character.health.copy(value = it.character.health.value.copy(current = it.character.health.value.current - healthToReduceTotal ))
                    )
                )
            }
        }
    }

    fun removeFirstMessage() {
        val newList = state.mainText.toMutableList().ifEmpty { return }
        newList.removeFirst()
        reduce { it.copy(mainText = newList) }
    }

    fun addItemToInventory(item: InventoryItem, count: Int, silent: Boolean = false) {
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
        if (!silent) showMessage("added ${item.title} ($count)")
        Log.i("addItemToInventory", state.getInventoryTexted())
    }

    fun removeItemFromInventory(item: InventoryItem, count: Int, silent: Boolean = false) {
        if (count<=0) { showMessage("count<=0"); return }
        if (state.inventory.keys.contains(item)) {
            val itemCount = state.inventory[item]!!
            if (itemCount>1) {
                val newInventory = state.inventory.mapValues { (inventoryItem, inventoryItemCount) ->
                    if (inventoryItem==item) inventoryItemCount - count
                    else inventoryItemCount
                }
                reduce { it.copy(inventory = newInventory) }
                if (!silent) showMessage("removed 1 ${item.title}")
            } else {
                val newInventory = state.inventory.minus(item)
                reduce { it.copy(inventory = newInventory) }
                if (!silent) showMessage("removed last ${item.title}")
            }
        } else showMessage("no items!")
        Log.i("removeItemFromInventory", state.getInventoryTexted())
    }

    fun useItem(item: InventoryItem) {
        when(val type = item.type) {
            is ItemType.Armor -> {
                showMessage("not usable!: ${item.title}")
            }
            is ItemType.Food -> {
                when(type.foodType) {
                    FoodType.Eat -> reduce {
                        val valueToReduce = state.character.hunger.value.current+type.restoreAmount
                        it.copy(character = state.character.copy(hunger = state.character.hunger.copy(value = state.character.hunger.value.copy(
                            current = if (valueToReduce>state.character.hunger.value.max) state.character.hunger.value.max else valueToReduce
                        ))))
                    }
                    FoodType.Drink -> reduce {
                        val valueToReduce = state.character.trist.value.current+type.restoreAmount
                        it.copy(character = state.character.copy(trist = state.character.trist.copy(value = state.character.trist.value.copy(
                            current = if (valueToReduce>state.character.trist.value.max) state.character.trist.value.max else valueToReduce
                        ))))
                    }
                }
                removeItemFromInventory(item, 1, true)
                showMessage("used: ${item.title}")
            }
            ItemType.Gold -> {
                showMessage("not usable!: ${item.title}")
            }
            is ItemType.Potion -> {
                when(type.potionType) {
                    PotionType.Health -> reduce {
                        val valueToReduce = state.character.health.value.current+type.restoreAmount
                        it.copy(character = state.character.copy(health = state.character.health.copy(value = state.character.health.value.copy(
                            current = if (valueToReduce>state.character.health.value.max) state.character.health.value.max else valueToReduce
                        ))))
                    }
                    PotionType.Stamina -> reduce {
                        val valueToReduce = state.character.stamina.value.current+type.restoreAmount
                        it.copy(character = state.character.copy(stamina = state.character.stamina.copy(value = state.character.stamina.value.copy(
                            current = if (valueToReduce>state.character.stamina.value.max) state.character.stamina.value.max else valueToReduce
                        ))))
                    }
                }
                removeItemFromInventory(item, 1, true)
                showMessage("used: ${item.title}")
            }
            is ItemType.Weapon -> {
                val newItem = item.copy(type = type.copy(durability = type.durability.copy(current = type.durability.current-1)))
                if (type.durability.current > 1) addItemToInventory(newItem, 1, true)
                removeItemFromInventory(item, 1, true)
                statTick(5, 2)
                showMessage("used: ${item.title}")
            }
        }
    }

    fun clearInventory() {
        state.inventory.ifEmpty { return }
        reduce { it.copy(inventory = emptyMap()) }
        Log.i("clearInventory", state.getInventoryTexted())
    }
}