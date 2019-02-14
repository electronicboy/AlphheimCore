/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import java.time.Duration
import java.util.*

class CombatTagHandler(plugin: ApertureCore) : AbstractHandler(plugin), Listener {

    private val activeTimers = LinkedHashMap<UUID, CombatTagEntry>()
    private val timerTask: BukkitRunnable

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        timerTask = object : BukkitRunnable() {
            override fun run() {
                val iter = activeTimers.iterator()

                for (entry in iter) {
                    if (!entry.value.update()) {
                        entry.value.remove()
                        iter.remove()
                    }
                }
            }
        }

        timerTask.runTaskTimer(plugin, 2, 5)
    }

    @EventHandler
    fun playerLogged(e: PlayerJoinEvent) {
        activeTimers.put(e.player.uniqueId, CombatTagEntry(e.player, System.currentTimeMillis() + Duration.ofSeconds(50).toMillis()))
    }

    override fun onDisable() {
        timerTask.cancel()
        for (timer in activeTimers) {
            timer.value.remove()
        }
        activeTimers.clear()
    }

}