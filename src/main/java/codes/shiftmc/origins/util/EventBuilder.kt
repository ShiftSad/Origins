package codes.shiftmc.origins.util

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * Example usage:
 * ```kt
 * JavaPlugin.events {
 *    event<PlayerJoinEvent>(
 *      priority = EventPriority.HIGHEST,
 *      permission = "my.permission",
 *    ) {
 *      player.sendMessage("Welcome!")
 *    }
 *
 *    suspendedEvent<PlayerQuitEvent>(
 *      dispatcher = minecraftDispatcher
 *    ) {
 *      delay(1000)
 *      println("Player quit!")
 *    }
 * }
 * ```
 */

class EventRegistry(val plugin: JavaPlugin) {
    val eventHandlers = mutableMapOf<Class<*>, EventData>()

    data class EventData(val handler: (Event) -> Unit, val priority: EventPriority, val permission: String?, val ignoreCancelled: Boolean)

    inline fun <reified T : Event> event(priority: EventPriority = EventPriority.NORMAL, permission: String? = null, ignoreCancelled: Boolean = false, noinline handler: T.() -> Unit) {
        eventHandlers[T::class.java] = EventData(handler as (Event) -> Unit, priority, permission, ignoreCancelled)
    }

    fun register() {
        eventHandlers.forEach { (eventClass, eventData) ->
            val (handler, priority, permission, ignoreCancelled) = eventData
            plugin.server.pluginManager.registerEvent(
                eventClass as Class<out Event>,
                object : Listener {},
                priority,
                { _, event ->
                    if (permission == null || event is PlayerEvent && event.player.hasPermission(permission)) {
                        handler(event)
                    }
                },
                plugin,
                ignoreCancelled
            )
        }
    }
}

fun JavaPlugin.events(init: EventRegistry.() -> Unit) {
    val pluginEvents = EventRegistry(this)
    pluginEvents.init()
    pluginEvents.register()
}
