package codes.shiftmc.origins.abilities

import codes.shiftmc.origins.origin.Ability
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

class SharpEater : Ability {

    override fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (item.type !in listOf(
            Material.COAL, Material.GOLD_ORE
        )) return

        val food = item.itemMeta.food
        when (item.type) {
            Material.COAL -> {
                // TODO -> Add custom potion effect, auto smelting
                food.nutrition = 4
                food.saturation = 2.5f
                food.eatSeconds = 4.0f
                food.setCanAlwaysEat(true)
            }
            else -> {
                food.nutrition = 6
                food.saturation = 3.0f
                food.eatSeconds = 6.0f
                food.setCanAlwaysEat(true)
            }
        }

        item.itemMeta.setFood(food)
    }
}

