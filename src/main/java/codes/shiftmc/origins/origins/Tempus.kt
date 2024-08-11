package codes.shiftmc.origins.origins

import codes.shiftmc.origins.origin.Ability
import codes.shiftmc.origins.origin.Impact
import codes.shiftmc.origins.origin.Origin
import codes.shiftmc.origins.util.scheduleDelayedTask
import codes.shiftmc.origins.util.toMiniMessage
import dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper
import dev.lone.itemsadder.api.FontImages.PlayerQuantityHudWrapper
import io.papermc.paper.entity.TeleportFlag.Relative
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.persistence.PersistentDataType


class Tempus : Origin {
    override fun identifier(): String =
        "tempus"

    override fun displayName(): Component =
        "Tempus".toMiniMessage()

    override fun impact(): Impact = Impact.LOW

    override fun description(): Component =
        """
            Você pode voltar no tempo, mas não pode mudar o passado.
        """.trimIndent().toMiniMessage()

    override fun abilities(): List<Ability> =
        listOf(BackInTime())

}

class BackInTime : Ability {
    private val tickCooldown: Int = 2
    private val namespacedKey = NamespacedKey("origins", "movement")

    override fun onDisable(player: Player) {
        val pdc = player.persistentDataContainer
        pdc.set(namespacedKey, PersistentDataType.STRING, "")
        pdc.set(NamespacedKey("origins", "world"), PersistentDataType.STRING, "")
        pdc.set(NamespacedKey("origins", "ability_cooldown"), PersistentDataType.LONG, 0)
    }

    override fun tick(player: Player) {
        val pdc = player.persistentDataContainer
        val hud = getHud(player)

        val cooldown = pdc.get(NamespacedKey("origins", "ability_cooldown"), PersistentDataType.LONG) ?: 0
        val progress = (System.currentTimeMillis() - cooldown) / (abilityCooldown / 8.0) + 1

        // If it is done, hide the bar
        if (progress >= 9) {
            hud.isVisible = false
        } else {
            hud.floatValue = progress.toFloat()
            hud.isVisible = true
        }

        // Save movement into player
        if (player.ticksLived % tickCooldown != 0) return

        var movement = pdc.get(namespacedKey, PersistentDataType.STRING) ?: run {
            pdc.set(namespacedKey, PersistentDataType.STRING, "")
            return
        }

        val world = pdc.get(NamespacedKey("origins", "world"), PersistentDataType.STRING) ?: run {
            pdc.set(NamespacedKey("origins", "world"), PersistentDataType.STRING, player.world.name)
            pdc.set(namespacedKey, PersistentDataType.STRING, "")
            return
        }

        if (player.world.name != world) {
            pdc.set(NamespacedKey("origins", "world"), PersistentDataType.STRING, player.world.name)
            pdc.set(namespacedKey, PersistentDataType.STRING, "")
            return
        }

        if (movement.split(";").size > 8.0 / (tickCooldown / 20.0)) {
            movement = movement.split(";").drop(1).joinToString(";")
        }

        // Save style
        // x,y,z;x,y,z;x,y,z
        val newMovement = movement + ";${player.location.x},${player.location.y},${player.location.z}"
        pdc.set(namespacedKey, PersistentDataType.STRING, newMovement)
    }

    override fun onEntityDamage(event: EntityDamageEvent) {
        val pdc = event.entity.persistentDataContainer
        if (pdc.get(NamespacedKey("origins", "immunity"), PersistentDataType.BOOLEAN) == true) {
            event.isCancelled = true
        }
    }

    override fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
        val pdc = event.player.persistentDataContainer
        event.player.sendMessage("§cParou.")
        pdc.set(NamespacedKey("origins", "going_back"), PersistentDataType.BOOLEAN, false)
    }

    override fun onPlayerDeath(event: PlayerDeathEvent) {
        val pdc = event.entity.persistentDataContainer
        pdc.set(namespacedKey, PersistentDataType.STRING, "")
        pdc.set(NamespacedKey("origins", "world"), PersistentDataType.STRING, "")
        pdc.set(NamespacedKey("origins", "ability_cooldown"), PersistentDataType.LONG, 0)
    }

    private val abilityCooldown = 10 * 1000

    override fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {
        // Tempus can't swap items
        event.isCancelled = true

        val player = event.player
        val pdc = player.persistentDataContainer
        val cooldown = pdc.get(NamespacedKey("origins", "ability_cooldown"), PersistentDataType.LONG) ?: 0
        if (System.currentTimeMillis() - cooldown < abilityCooldown) {
            player.sendMessage("§cVocê não pode usar essa habilidade agora.")
            return
        }

        val hud = getHud(player)
        hud.isVisible = true
        hud.floatValue = 1.0f
        pdc.set(NamespacedKey("origins", "ability_cooldown"), PersistentDataType.LONG, System.currentTimeMillis())
        playBackInTime(player)
    }

    private fun playBackInTime(player: Player) {
        val pdc = player.persistentDataContainer
        val movement = pdc.get(namespacedKey, PersistentDataType.STRING) ?: return

        val playerMovement = movement.split(";").map {
            val (x, y, z) = it.split(",").map { it.toDouble() }
            if (x.isNaN() || y.isNaN() || z.isNaN()) return@map null
            Vec(x, y, z)
        }.filterNotNull().reversed()

        pdc.set(NamespacedKey("origins", "going_back"), PersistentDataType.BOOLEAN, true)
        pdc.set(NamespacedKey("origins", "immunity"), PersistentDataType.BOOLEAN, true)

        for (i in 0 until playerMovement.size - 1) {
            val currentVec = playerMovement[i]
            val nextVec = playerMovement[i + 1]

            val dx = (nextVec.x - currentVec.x) / tickCooldown
            val dy = (nextVec.y - currentVec.y) / tickCooldown
            val dz = (nextVec.z - currentVec.z) / tickCooldown

            for (j in 0 until tickCooldown) {
                scheduleDelayedTask((i * tickCooldown + j).toLong()) {
                    if (pdc.get(
                            NamespacedKey("origins", "going_back"),
                            PersistentDataType.BOOLEAN
                        ) != true
                    ) return@scheduleDelayedTask

                    val interpolatedLocation = Location(
                        player.world,
                        currentVec.x + dx * j,
                        currentVec.y + dy * j,
                        currentVec.z + dz * j,
                        player.yaw,
                        player.pitch,
                    )
                    player.teleport(interpolatedLocation, Relative.YAW, Relative.PITCH)

                    if (i == playerMovement.size - 2 && j == tickCooldown - 1) {
                        pdc.set(NamespacedKey("origins", "going_back"), PersistentDataType.BOOLEAN, false)
                    }
                }
            }
        }

        scheduleDelayedTask((playerMovement.size * tickCooldown + 20).toLong()) {
            pdc.set(NamespacedKey("origins", "immunity"), PersistentDataType.BOOLEAN, false)
        }

        pdc.set(namespacedKey, PersistentDataType.STRING, "")
    }

    private fun getHud(player: Player): PlayerQuantityHudWrapper {
        val playerHudsHolderWrapper = PlayerHudsHolderWrapper(player)
        return PlayerQuantityHudWrapper(playerHudsHolderWrapper, "origins:tempus_bar")
    }
}

private data class Vec(val x: Double, val y: Double, val z: Double)
