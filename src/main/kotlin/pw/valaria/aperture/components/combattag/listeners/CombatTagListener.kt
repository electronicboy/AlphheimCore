/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag.listeners

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Tameable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import pw.valaria.aperture.components.combattag.CombatTagHandler
import java.time.Duration

class CombatTagListener(val handler: CombatTagHandler) : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun playerHitPlayer(ev: EntityDamageByEntityEvent) {
        val target = ev.entity as? Player ?: return

        val damagingEntity = ev.damager

        if (target.gameMode == GameMode.CREATIVE) return // Don't tag creative players

        var damager: Player? = null;

        if (damagingEntity is Player) {
            damager = damagingEntity
        } else if (damagingEntity is Projectile) {
            if (damagingEntity.shooter is Player) {
                damager = damagingEntity.shooter as Player
            }
        } else if (damagingEntity is Tameable) {
            val owner = damagingEntity.owner
            if (owner != null && owner is Player) {
                damager = owner
            }
        }

        if (damager == null) return // Nobody tagged us?

        if (target == damager) return // Don't tag self

        handler.startOrUpdateTimer(target, Duration.ofSeconds(50))

    }

    @EventHandler
    fun playerQuit(ev: PlayerQuitEvent) {
        val tag = handler.getTag(ev.player)

        if (tag == null || !tag.isActive()) return

        // RIP
        ev.player.health = 0.0
    }


    @EventHandler
    fun playerKick(ev: PlayerKickEvent) {
        handler.getTag(ev.player)?.remove()
    }
}