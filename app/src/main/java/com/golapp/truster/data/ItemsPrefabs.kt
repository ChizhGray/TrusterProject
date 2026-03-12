package com.golapp.truster.data

enum class ItemsPrefabs(val item: InventoryItem) {
    Branch(
        InventoryItem(
            "Branch",
            ItemType.Weapon(
                WeaponStat(
                    WeaponType.MeleeOneHand,
                    Durability(10, 10),
                    7,
                    5
                )
            ),
            "Branch from tree, simple weapon"
        )
    ),
    Stone(
        InventoryItem(
            "Stone",
            ItemType.Weapon(
                WeaponStat(
                    WeaponType.MeleeOneHand,
                    Durability(10, 10),
                    13,
                    6
                )
            ),
            "Stone from ground, simple weapon"
        )
    ),
    Sword(
        InventoryItem(
            "Sword",
            ItemType.Weapon(
                WeaponStat(
                    WeaponType.MeleeOneHand,
                    Durability(50, 50),
                    55,
                    10
                )
            ),
            "Good weapon to defeat enemies"
        )
    ),
    Bread(
        InventoryItem(
            "Bread",
            ItemType.Food(
                FoodStat(
                    30,
                    FoodType.Eat
                )
            ),
            "Food, restore hunger"
        )
    ),
    Butter(
        InventoryItem(
            "Butter",
            ItemType.Food(
                FoodStat(
                    50,
                    FoodType.Eat
                )
            ),
            "Food, restore hunger"
        )
    ),
    Water(
        InventoryItem(
            "Water",
            ItemType.Food(
                FoodStat(
                    15,
                    FoodType.Drink
                )
            ),
            "Drinkable liquid"
        )
    ),
    HolyWater(
        InventoryItem(
            "Holly Water",
            ItemType.Food(
                FoodStat(
                    55,
                    FoodType.Drink
                )
            ),
            "Drinkable liquid"
        )
    ),
    HP(
        InventoryItem(
            "HP",
            ItemType.Potion(
                PotionStat(
                    15,
                    PotionType.Health
                )
            ),
            "Health Potion"
        )
    ),
    SP(
        InventoryItem(
            "SP",
            ItemType.Potion(
                PotionStat(
                    15,
                    PotionType.Stamina
                )
            ),
            "Stamina Potion"
        )
    )
}