package codes.shiftmc.origins.handlers

import codes.shiftmc.origins.origin.OriginRegistry
import codes.shiftmc.origins.util.events
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.plugin.java.JavaPlugin

fun eventHandlers(plugin: JavaPlugin) {
    plugin.events {
        fun origin(event: Event) {
            val player = when (event) {
                is PlayerEvent -> event.player
                is EntityEvent -> event.entity as? Player
                else -> return
            } ?: return

            val origin = OriginRegistry.getOrigin(player)
            when (event) {
                is PlayerInteractEntityEvent -> origin.abilities().forEach { it.onPlayerInteractEntity(event) }
                is PlayerBedEnterEvent -> origin.abilities().forEach { it.onPlayerBedEnter(event) }
                is PlayerItemConsumeEvent -> origin.abilities().forEach { it.onPlayerItemConsume(event) }
                is PlayerSwapHandItemsEvent -> origin.abilities().forEach { it.onPlayerSwapHandItems(event) }
                is PlayerToggleSneakEvent -> origin.abilities().forEach { it.onPlayerToggleSneak(event) }
                is PlayerDeathEvent -> origin.abilities().forEach { it.onPlayerDeath(event) }
            }
        }

        event<EntityDamageEvent>(::origin)
        event<PlayerInteractEntityEvent>(::origin)
        event<PlayerBedEnterEvent>(::origin)
        event<PlayerItemConsumeEvent>(::origin)
        event<PlayerSwapHandItemsEvent>(::origin)
        event<PlayerToggleSneakEvent>(::origin)
        event<PlayerDeathEvent>(::origin)
    }
}