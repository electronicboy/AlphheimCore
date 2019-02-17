/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.combattag.commands.CommandCombatTag
import java.time.Duration
import java.util.*

class CombatTagHandler(plugin: ApertureCore) : AbstractHandler(plugin), Listener {

    private val activeTimers = LinkedHashMap<UUID, CombatTagEntry>()
    private val timerTask: BukkitRunnable

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        timerTask = object : BukkitRunnable() {
            override fun run() {
                var timers: LinkedHashMap<UUID, CombatTagEntry>
                synchronized(this) {
                    @Suppress("UNCHECKED_CAST")
                    timers = activeTimers.clone() as LinkedHashMap<UUID, CombatTagEntry>
                }

                for (entry in timers) {
                    if (!entry.value.update()) {
                        synchronized(this) {
                            entry.value.remove()
                            activeTimers.remove(entry.key)
                            return // We're not returning anything kotlin....
                        }
                    }
                }
            }
        }

        timerTask.runTaskTimerAsynchronously(plugin, 1, 1)

        plugin.commandManager.registerCommand(CommandCombatTag(plugin), true)
    }





    @Synchronized
    fun startOrUpdateTimer(player: Player, duration: Duration) {
        val current = activeTimers[player.uniqueId]
        if (current != null && current.getRemainingDuration().toMillis() < duration.toMillis()) {
            current.bump(duration)
        }

        val ret = CombatTagEntry(player, duration)
        activeTimers.putIfAbsent(player.uniqueId, ret)
    }


    @EventHandler
    fun playerLogged(e: PlayerJoinEvent) {
        activeTimers.put(e.player.uniqueId, CombatTagEntry(e.player, Duration.ofSeconds(70).toMillis()))
    }

    @Synchronized
    override fun onDisable() {
        timerTask.cancel()
        for (timer in activeTimers) {
            timer.value.remove()
        }
        activeTimers.clear()
    }

}