package codes.shiftmc.origins

import codes.shiftmc.origins.handlers.eventHandlers
import codes.shiftmc.origins.origin.OriginRegistry
import codes.shiftmc.origins.origins.*
import codes.shiftmc.origins.util.setStaticPlugin
import org.bukkit.plugin.java.JavaPlugin

class Origins : JavaPlugin() {

    companion object {
        lateinit var instance: Origins
    }

    override fun onEnable() {
        instance = this
        setStaticPlugin(this)

        OriginRegistry.enable(this)
        OriginRegistry.register(Inchling())
        OriginRegistry.register(Human())
        OriginRegistry.register(Avian())
        OriginRegistry.register(Merling())
        OriginRegistry.register(Tempus())
        OriginCommand

        eventHandlers(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
