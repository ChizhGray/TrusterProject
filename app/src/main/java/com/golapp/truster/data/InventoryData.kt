package com.golapp.truster.data

data class InventoryItem(
    val title: String,
    val type: ItemType,
    val description: String
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

data class FoodStat(
    val restoreAmount: Int,
    val foodType: FoodType
)

enum class BodyType {
    Head, Body,
    ArmLeft, ArmRight,
    LegLeft, LegRight
}

data class ArmorStat(
    val bodyType: BodyType,
    val durability: Durability,
    val defence: Map<BodyType, Int>
)

data class WeaponStat(
    val weaponType: WeaponType,
    val durability: Durability,
    val attack: Int,
    val stamina: Int
)

enum class WeaponType {
    MeleeOneHand, MeleeTwoHand,
    RangeOneHand, RangeTwoHand
}

data class PotionStat(
    val restoreAmount: Int,
    val potionType: PotionType
)

enum class PotionType {
    Health, Stamina
}



sealed interface ItemType {
    data object Gold: ItemType
    data class Food(val stat: FoodStat): ItemType
    data class Potion(val stat: PotionStat): ItemType
    data class Armor(val stat: ArmorStat): ItemType
    data class Weapon(val stat: WeaponStat): ItemType
}