package codes.shiftmc.origins.origins

import codes.shiftmc.origins.origin.Ability
import codes.shiftmc.origins.origin.Impact
import codes.shiftmc.origins.origin.Origin
import codes.shiftmc.origins.util.getStaticPlugin
import codes.shiftmc.origins.util.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Merling : Origin {
    override fun identifier(): String =
        "merling"

    override fun displayName(): Component =
        "Sereia".toMiniMessage()

    override fun impact(): Impact =
        Impact.HIGH

    override fun description(): Component =
        """
            Tu é uma seria, tipo, literalmente
            Vc deve ser bem antisocial :|
        """.trimIndent().toMiniMessage()

    override fun abilities(): List<Ability> =
        listOf(Gills(), AquaAffinity())
}

class Gills : Ability {

    override fun tick(player: Player) {
        // TODO -> Find a smarter way to do this.
        if (player.isUnderWater) player.remainingAir = (player.remainingAir + 4).coerceAtMost(player.maximumAir)
        else {
            val random = (0..5).random()
            val enchantmentLevel = player.inventory.helmet?.getEnchantmentLevel(Enchantment.RESPIRATION) ?: 0
            val level = when (enchantmentLevel) {
                0 -> 1
                1 -> 2
                2 -> 3
                3 -> 5
                else -> 1
            }
            if (random >= level)
                player.remainingAir = (player.remainingAir - 5).coerceAtLeast(-5)
            else player.remainingAir = (player.remainingAir - 4).coerceAtLeast(-5)

            if (player.remainingAir <= 2) {
                player.damage(1.0)
                // TODO -> Custom death message
            }
        }
    }
}

class AquaAffinity : Ability {

    private val diggingSpeed = Attribute.PLAYER_BLOCK_BREAK_SPEED to AttributeModifier(
        "aqua-affinity", 9.0, Operation.ADD_NUMBER
    )
    private val nightVision = PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0, false, false)
    private val dolphinGrace = PotionEffect(PotionEffectType.DOLPHINS_GRACE, Int.MAX_VALUE, 0, false, false)

    override fun tick(player: Player) {
        // Give the digging speed to the player whenever he is underwater
        if (player.isUnderWater) player.addAttributeModifier(diggingSpeed.first, diggingSpeed.second)
        else player.removeAttribute(diggingSpeed.second.name, diggingSpeed.first)

        // Give night vision and dolphin grace to the player whenever he is underwater
        if (player.isUnderWater) {
            applyEffect(player, nightVision)
            applyEffect(player, dolphinGrace)
        } else {
            removeEffect(player, nightVision)
            removeEffect(player, dolphinGrace)
        }
    }

    private fun applyEffect(player: Player, effect: PotionEffect) {
        player.scheduler.run(getStaticPlugin(), {
            player.addPotionEffect(effect)
        }, null)
    }

    private fun removeEffect(player: Player, effect: PotionEffect) {
        player.scheduler.run(getStaticPlugin(), {
            player.removePotionEffect(effect.type)
        }, null)
    }
}



fun Player.addAttributeModifier(attribute: Attribute, modifier: AttributeModifier) {
    if (this.getAttribute(attribute)?.modifiers?.any { it.name == modifier.name } == true) return
    this.getAttribute(attribute)?.addModifier(modifier)
}

fun Player.removeAttribute(name: String, attribute: Attribute) {
    val modifier = this.getAttribute(attribute)?.modifiers?.firstOrNull { it.name == name } ?: return
    this.getAttribute(attribute)?.removeModifier(modifier)
}