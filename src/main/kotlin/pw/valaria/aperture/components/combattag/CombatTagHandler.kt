/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.combattag.commands.CommandCombatTag
import pw.valaria.aperture.components.combattag.listeners.CombatTagListener
import java.time.Duration
import java.util.*

class CombatTagHandler(plugin: ApertureCore) : AbstractHandler(plugin), Listener {

    private val activeTimers = LinkedHashMap<UUID, CombatTagEntry>()
    private val timerTask: BukkitRunnable

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        timerTask = object : BukkitRunnable() {
            override fun run() {
                synchronized(activeTimers) {
                    for (entry in activeTimers) {
                        if (!entry.value.update()) {
                            entry.value.remove()
                            activeTimers.remove(entry.key)
                        }
                    }
                }
            }
        }

        timerTask.runTaskTimerAsynchronously(plugin, 1, 1)

        plugin.commandManager.registerCommand(CommandCombatTag(plugin, this), true)

        Bukkit.getPluginManager().registerEvents(CombatTagListener(this), plugin)
    }

    fun startOrUpdateTimer(player: Player, duration: Duration): CombatTagEntry {
        synchronized(activeTimers) {
            val current = activeTimers[player.uniqueId]
            if (current != null && current.getRemainingDuration().toMillis() < duration.toMillis()) {
                current.bump(duration)
                return current
            } else {
                player.sendActionBar("${ChatColor.RED}- You have been tagged! -")
            }

            val ret = CombatTagEntry(player, duration)
            activeTimers.putIfAbsent(player.uniqueId, ret)
            return ret
        }
    }

    fun getTag(player: Player): CombatTagEntry? {
        return getTag(player.uniqueId)
    }

    fun getTag(player: UUID): CombatTagEntry? {
        return activeTimers[player]
    }


    override fun onDisable() {
        timerTask.cancel()
        for (timer in activeTimers) {
            timer.value.remove()
        }
        activeTimers.clear()
    }

}