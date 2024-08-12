package codes.shiftmc.origins.origin

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

interface Ability {

    fun tick(player: Player) {}
    fun onEnable(player: Player) {}
    fun onDisable(player: Player) {}

    fun onEntityDamage(event: EntityDamageEvent) {}
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {}
    fun onPlayerBedEnter(event: PlayerBedEnterEvent) {}
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {}
    fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {}
    fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {}
    fun onPlayerDeath(event: PlayerDeathEvent) {}
}