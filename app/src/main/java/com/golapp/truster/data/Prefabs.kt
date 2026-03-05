package com.golapp.truster.data

enum class Prefabs(val item: InventoryItem) {
    Branch(
        InventoryItem(
            "Branch",
            ItemType.Weapon(
                Durability(10, 10),
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