package codes.shiftmc.origins.origins

import codes.shiftmc.origins.origin.Ability
import codes.shiftmc.origins.origin.Impact
import codes.shiftmc.origins.origin.Origin
import codes.shiftmc.origins.util.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

class Avian : Origin {
    override fun identifier(): String =
        "avian"

    override fun displayName(): Component =
        "Ave".toMiniMessage()

    override fun impact(): Impact =
        Impact.LOW

    override fun description(): Component =
        """
            Tu é um passarinho, vegetariano.
            
            Tu é leve, ou seja, a gravidade se aplica diferentemente em você,
            e já que tu é passarinho, tu só gosta de dormi alto, faz mal
            ter tudo isso de ar no pulmão. A pressão atmosférica é muito
            alta aqui em baixo, logo, só pode dormir acima de 83 blocos.
        """.trimIndent().toMiniMessage()

    override fun abilities(): List<Ability> =
        listOf(Vegetarian(), LikeAir(), FreshAir(), SlowFalling())
}

class Vegetarian : Ability {

    private val allowedFoods = setOf(
        Material.APPLE, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE, Material.MELON_SLICE,
        Material.SWEET_BERRIES, Material.SWEET_BERRIES, Material.GLOW_BERRIES, Material.CHORUS_FRUIT,
        Material.CARROT, Material.GOLDEN_CARROT, Material.POTATO, Material.BAKED_POTATO, Material.BEETROOT,
        Material.DRIED_KELP, Material.BREAD, Material.COOKIE, Material.CAKE, Material.PUMPKIN_PIE,
        Material.MUSHROOM_STEW, Material.BEETROOT_SOUP, Material.SUSPICIOUS_STEW
    )

    override fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        val item = event.item
        if (item.type !in allowedFoods) {
            event.isCancelled = true
            event.player.sendActionBar("Você é vegetariano!".toMiniMessage())
        }
    }
}

class LikeAir : Ability {

    private val modifiers = mapOf(
        Attribute.GENERIC_SAFE_FALL_DISTANCE to AttributeModifier("like-air", 1.0, Operation.ADD_NUMBER),
        Attribute.GENERIC_MOVEMENT_SPEED     to AttributeModifier("like-air", 0.02, Operation.ADD_NUMBER),
    )

    override fun onEnable(player: Player) {
        modifiers.forEach { (attribute, modifier) ->
            player.getAttribute(attribute)?.addModifier(modifier)
        }
    }

    override fun onDisable(player: Player) {
        modifiers.forEach { (attribute, modifier) ->
            val playerAttribute = player.getAttribute(attribute) ?: throw IllegalStateException("Attribute $attribute not found")
            val toRemove = playerAttribute.modifiers.filter { it.name == modifier.name }
            toRemove.forEach {
                playerAttribute.removeModifier(it)
            }
        }
    }
}

class FreshAir : Ability {

    override fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
        val bed = event.bed
        if (bed.y < 86) {
            event.setUseBed(Event.Result.DENY)
            event.player.sendActionBar("Você precisa de uma menor pressão atmosférica para dormir".toMiniMessage())
        }
    }
}

class SlowFalling : Ability {

    override fun onEnable(player: Player) {
        player.getAttribute(Attribute.GENERIC_GRAVITY)?.addModifier(
            AttributeModifier("slow-falling", -0.02, Operation.ADD_NUMBER)
        )
    }

    override fun onDisable(player: Player) {
        val attribute = player.getAttribute(Attribute.GENERIC_GRAVITY)
        val modifier = attribute?.modifiers?.firstOrNull { it.name == "slow-falling" }
        if (modifier != null) attribute.removeModifier(modifier)
    }
}