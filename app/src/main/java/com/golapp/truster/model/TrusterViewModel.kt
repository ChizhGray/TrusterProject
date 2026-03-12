package com.golapp.truster.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golapp.truster.data.*
import com.golapp.truster.functions.getChance
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
        repeat(6) {
            if (getChance(60)) addItemToInventory(ItemsPrefabs.entries.random().item, 1, true)
        }
    }

    fun statTick(trist: Int, hunger: Int, enemyAttack: Int?) {
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
        val healthToReduceTotal = healthByTristNew + healthByHungerNew + (enemyAttack ?: 0)

        if (healthToReduceTotal!=0) {
            reduce {
                it.copy(
                    character = it.character.copy(
                        health = it.character.health.copy(value = it.character.health.value.copy(current = it.character.health.value.current - healthToReduceTotal))
                    )
                )
            }
        }
    }

    fun removeFirstMessage() {
        val newList = state.mainText.toMutableList().ifEmpty { return }
        newList.remove(newList[0])
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
                when(type.stat.foodType) {
                    FoodType.Eat -> reduce {
                        val valueToReduce = state.character.hunger.value.current+type.stat.restoreAmount
                        it.copy(character = state.character.copy(hunger = state.character.hunger.copy(value = state.character.hunger.value.copy(
                            current = if (valueToReduce>state.character.hunger.value.max) state.character.hunger.value.max else valueToReduce
                        ))))
                    }
                    FoodType.Drink -> reduce {
                        val valueToReduce = state.character.trist.value.current+type.stat.restoreAmount
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
                when(type.stat.potionType) {
                    PotionType.Health -> reduce {
                        val valueToReduce = state.character.health.value.current+type.stat.restoreAmount
                        it.copy(character = state.character.copy(health = state.character.health.copy(value = state.character.health.value.copy(
                            current = if (valueToReduce>state.character.health.value.max) state.character.health.value.max else valueToReduce
                        ))))
                    }
                    PotionType.Stamina -> reduce {
                        val valueToReduce = state.character.stamina.value.current+type.stat.restoreAmount
                        it.copy(character = state.character.copy(stamina = state.character.stamina.copy(value = state.character.stamina.value.copy(
                            current = if (valueToReduce>state.character.stamina.value.max) state.character.stamina.value.max else valueToReduce
                        ))))
                    }
                }
                removeItemFromInventory(item, 1, true)
                showMessage("used: ${item.title}")
            }
            is ItemType.Weapon -> {
                state.enemy?.let { enemy ->
                    if (enemy.hp.current == 0) {
                        showMessage("enemy is dead")
                    } else if (staminaReduce(type.stat.stamina)) {
                        if (type.stat.durability.current-1 >= 0) {
                            reduce {
                                it.copy(
                                    inventory = it.inventory.mapKeys {
                                        if (it.key == item) {
                                            item.copy(
                                                type = type.copy(
                                                    stat = type.stat.copy(
                                                        durability = type.stat.durability.copy(
                                                            current = type.stat.durability.current-1)
                                                    )
                                                )
                                            )
                                        } else it.key
                                    }
                                )
                            }
                            attackEnemy(type.stat.attack)
                            statTick(5, 2, enemy.attack)
                            showMessage("used: ${item.title}")
                        } else {
                            showMessage("weapon is broken")
                        }
                    }
                } ?: showMessage("no target to attack")
            }
        }
    }

    private fun attackEnemy(amount: Int) {
        state.enemy?.let { enemy ->
            val enemyHP = enemy.hp.current-amount
            reduce {
                it.copy(
                    enemy = it.enemy?.copy(
                        hp = enemy.hp.copy(
                            current = if (enemyHP >= 0) enemyHP else 0
                        )
                    )
                )
            }
            if (enemy.hp.current == 0) {
                addItemToInventory(ItemsPrefabs.entries.random().item, 1)
            }
        }
    }

    fun staminaReduce(amount: Int): Boolean {
        val staminaToReduce = state.character.stamina.value.current-amount
        if (staminaToReduce >= 0) {
            reduce {
                it.copy(
                    character = it.character.copy(
                        stamina = it.character.stamina.copy(value = it.character.stamina.value.copy(current = it.character.stamina.value.current-amount) )
                    )
                )
            }
            return true
        } else {
            showMessage("not enough stamina")
            return false
        }
    }

    fun clearInventory() {
        state.inventory.ifEmpty { return }
        reduce { it.copy(inventory = emptyMap()) }
        Log.i("clearInventory", state.getInventoryTexted())
    }

    fun editEnemy(enemy: Enemy?) {
        reduce { it.copy(enemy = enemy) }
        Log.i("editEnemy", "enemy set $enemy")
    }

    fun equipWeapon(weapon: InventoryItem?) {
        reduce {
            it.copy(
                character = it.character.copy(
                    equippedItem = weapon
                )
            )
        }
    }
}