package com.golapp.truster.data

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