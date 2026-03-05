package com.golapp.truster

enum class Prefabs(val item: InventoryItem) {
    Pistol(
        InventoryItem(
            "Pistol",
            ItemType.Weapon(
                Durability(5, 5),
                WeaponStat(
                    WeaponType.RangeOneHand,
                    5
                )
            )
        )
    ),
    Bread(
        InventoryItem(
            "Bread",
            ItemType.Food(
                30,
                FoodType.Eat
            )
        )
    ),
    Water(
        InventoryItem(
            "Water",
            ItemType.Food(
                15,
                FoodType.Drink
            )
        )
    ),
}