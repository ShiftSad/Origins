package codes.shiftmc.origins.abilities

import codes.shiftmc.origins.origin.Ability
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.Player

class BiteSized(
    health: Double = -0.5,
    movementSpeed: Double = -0.015,
    playerEntityInteractionRange: Double = -0.25,
    playerBlockInteractionRange: Double = -1.5,
    stepHeight: Double = 0.5,
    scale: Double = -0.75
) : Ability {
    private val modifiers = mutableMapOf(
        Attribute.GENERIC_MAX_HEALTH to AttributeModifier("bite-sized", health, Operation.MULTIPLY_SCALAR_1),
        Attribute.GENERIC_MOVEMENT_SPEED to AttributeModifier("bite-sized", movementSpeed, Operation.ADD_NUMBER),
        Attribute.PLAYER_ENTITY_INTERACTION_RANGE to AttributeModifier("bite-sized", playerEntityInteractionRange, Operation.ADD_NUMBER),
        Attribute.PLAYER_BLOCK_INTERACTION_RANGE to AttributeModifier("bite-sized", playerBlockInteractionRange, Operation.ADD_NUMBER),
        Attribute.GENERIC_STEP_HEIGHT to AttributeModifier("bite-sized", stepHeight, Operation.ADD_NUMBER),
        Attribute.GENERIC_SCALE to AttributeModifier("bite-sized", scale, Operation.ADD_NUMBER),
    )

    override fun onEnable(player: Player) {
        modifiers.forEach { (attribute, modifier) ->
            val playerAttribute = player.getAttribute(attribute) ?: throw IllegalStateException("Attribute $attribute not found")
            playerAttribute.addModifier(modifier)
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