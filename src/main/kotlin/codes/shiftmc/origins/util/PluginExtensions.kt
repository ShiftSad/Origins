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

fun scheduleRepeatingTask(delay: Long, period: Long, task: () -> Unit) {
    plugin ?: throw IllegalStateException("Plugin is not set. Please set the plugin using setStaticPlugin method.")
    plugin!!.server.scheduler.runTaskTimer(plugin!!, task, delay, period)
}

fun scheduleDelayedTask(delay: Long, task: () -> Unit) {
    plugin ?: throw IllegalStateException("Plugin is not set. Please set the plugin using setStaticPlugin method.")
    plugin!!.server.scheduler.runTaskLater(plugin!!, task, delay)
}
fun scheduleDelayedTask(delay: Int, task: () -> Unit) = scheduleDelayedTask(delay.toLong(), task)

fun scheduleNextTick(task: () -> Unit) {
    plugin ?: throw IllegalStateException("Plugin is not set. Please set the plugin using setStaticPlugin method.")
    plugin!!.server.scheduler.runTask(plugin!!, task)
}
