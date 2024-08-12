package codes.shiftmc.origins.util

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

private var plugin: JavaPlugin? = null

fun setStaticPlugin(javaPlugin: JavaPlugin) {
    plugin = javaPlugin
}

fun getStaticPlugin(): JavaPlugin {
    return plugin ?: throw IllegalStateException("Plugin is not set. Please set the plugin using setStaticPlugin method.")
}

fun JavaPlugin.registerEvents(vararg listeners: Listener) {
    listeners.forEach { server.pluginManager.registerEvents(it, this) }
}