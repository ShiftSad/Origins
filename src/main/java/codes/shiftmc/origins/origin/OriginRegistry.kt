package codes.shiftmc.origins.origin

import com.github.benmanes.caffeine.cache.Caffeine
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Method
import java.time.Duration

object OriginRegistry {
    private lateinit var originKey: NamespacedKey

    private val origins = mutableMapOf<String, Origin>()
    private val playerOriginCache = Caffeine.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(Duration.ofMinutes(10))
        .build { key: Player -> getUncachedOrigin(key) }

    private var enabled = false

    fun enable(plugin: JavaPlugin) {
        if (enabled) return
        enabled = true
        originKey = NamespacedKey(plugin, "origin")

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            // Tick
            Bukkit.getOnlinePlayers().forEach { player ->
                getOrigin(player).abilities().forEach { it.tick(player) }
            }
        }, 0, 1)
    }

    fun setOrigin(player: Player, origin: Origin) {
        val old = getOrigin(player)
        old.abilities().forEach { it.onDisable(player) }

        playerOriginCache.put(player, origin)
        player.persistentDataContainer.set(originKey, PersistentDataType.STRING, origin.identifier())
        origin.abilities().forEach { it.onEnable(player) }
    }

    fun getOrigins(): Collection<Origin> = origins.values.toList()
    fun getOrigin(identifier: String): Origin? = origins[identifier]
    fun getOrigin(player: Player): Origin = playerOriginCache.get(player)

    private fun getUncachedOrigin(player: Player): Origin {
        val pdc = player.persistentDataContainer
        val originKey = pdc.get(originKey, PersistentDataType.STRING) ?: return origins["none"]!!

        return origins[originKey]!!
    }

    fun register(origin: Origin) {
        origins[origin.identifier()] = origin
    }
}