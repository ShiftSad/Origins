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
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.player.PlayerInteractEntityEvent

class Inchling : Origin {
    override fun identifier(): String =
        "inchling"

    override fun displayName(): Component =
        "Anão".toMiniMessage()

    override fun impact(): Impact =
        Impact.HIGH

    override fun description(): Component =
        """
            Você tem aproximadamente 1 e 70 metros, é foda ser pequeno.
            Vc tem menos vida, mas é tipo, literalmente impossível de acertar um tapa.
            Perfeito para fazer umas pasagem secreta.
            
            Tu é leve, ou seja, nada de dano de queda, espinhos ou bate a cabeça
            com elytra. O céu é o limite.
        """.trimIndent().toMiniMessage()

    override fun abilities(): List<Ability> = listOf(
        BiteSized(), Jockey(), Nimble()
    )
}

class Nimble : Ability {
    override fun tick(player: Player) { }

    private val blockedDamageTypes = mutableSetOf(
        DamageCause.FLY_INTO_WALL, DamageCause.FALL,
        DamageCause.THORNS, DamageCause.CONTACT
    )

    override fun onEntityDamage(event: EntityDamageEvent) {
        if (event.cause in blockedDamageTypes) {
            event.isCancelled = true
        }
    }
}

class Jockey : Ability {

    private val allowedEntities = setOf(
        EntityType.PLAYER, EntityType.COW
    )

    override fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        val entity = event.rightClicked

        if (entity.type !in allowedEntities) return
        player.scheduler.run(getStaticPlugin(), {
            entity.addPassenger(player)
        }, null)
    }
}

class BiteSized : Ability {
    private val modifiers = mutableMapOf(
        Attribute.GENERIC_MAX_HEALTH to AttributeModifier("bite-sized", -0.5, Operation.MULTIPLY_SCALAR_1),
        Attribute.GENERIC_MOVEMENT_SPEED to AttributeModifier("bite-sized", -0.015, Operation.ADD_NUMBER),
        Attribute.PLAYER_ENTITY_INTERACTION_RANGE to AttributeModifier("bite-sized", -0.25, Operation.ADD_NUMBER),
        Attribute.PLAYER_BLOCK_INTERACTION_RANGE to AttributeModifier("bite-sized", -1.5, Operation.ADD_NUMBER),
        Attribute.GENERIC_STEP_HEIGHT to AttributeModifier("bite-sized", 0.5, Operation.ADD_NUMBER),
        Attribute.GENERIC_SCALE to AttributeModifier("bite-sized", -0.75, Operation.ADD_NUMBER),
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