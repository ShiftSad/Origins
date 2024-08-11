package codes.shiftmc.origins.handlers

import codes.shiftmc.origins.origin.OriginRegistry
import codes.shiftmc.origins.util.events
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.plugin.java.JavaPlugin

fun eventHandlers(plugin: JavaPlugin) {
    plugin.events {
        event<EntityDamageEvent> {
            if (this.entity !is Player) return@event
            val origin = OriginRegistry.getOrigin(this.entity as Player)
            origin.abilities().forEach { it.onEntityDamage(this) }
        }
        event<PlayerInteractEntityEvent> {
            val origin = OriginRegistry.getOrigin(this.player)
            origin.abilities().forEach { it.onPlayerInteractEntity(this) }
        }
        event<PlayerBedEnterEvent> {
            val origin = OriginRegistry.getOrigin(this.player)
            origin.abilities().forEach { it.onPlayerBedEnter(this) }
        }
        event<PlayerItemConsumeEvent> {
            val origin = OriginRegistry.getOrigin(this.player)
            origin.abilities().forEach { it.onPlayerItemConsume(this) }
        }
        event<PlayerSwapHandItemsEvent> {
            val origin = OriginRegistry.getOrigin(this.player)
            origin.abilities().forEach { it.onPlayerSwapHandItems(this) }
        }
        event<PlayerToggleSneakEvent> {
            val origin = OriginRegistry.getOrigin(this.player)
            origin.abilities().forEach { it.onPlayerToggleSneak(this) }
        }
        event<PlayerDeathEvent> {
            val origin = OriginRegistry.getOrigin(this.entity)
            origin.abilities().forEach { it.onPlayerDeath(this) }
        }
    }
}