package codes.shiftmc.origins.handlers

import codes.shiftmc.origins.origin.OriginRegistry
import codes.shiftmc.origins.util.events
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityEvent
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
            val eventClass = event::class
            origin.abilities().forEach {
                val method = it::class.java.declaredMethods.find { methods ->
                    methods.name == "on${eventClass.simpleName?.removeSuffix("Event")}"
                } ?: return@forEach

                method.invoke(it, event)
            }
        }

        event<EntityDamageEvent>(::origin)
        event<PlayerInteractEntityEvent>(::origin)
        event<PlayerBedEnterEvent>(::origin)
        event<PlayerItemConsumeEvent>(::origin)
        event<PlayerSwapHandItemsEvent>(::origin)
        event<PlayerToggleSneakEvent>(::origin)
        event<PlayerInteractEvent>(::origin)
    }
}