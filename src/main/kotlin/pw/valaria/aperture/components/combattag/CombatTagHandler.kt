/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag

import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import java.util.*

sealed class CombatTagHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    private val activeTimers = LinkedHashMap<UUID, CombatTagEntry>()
    private val timer: BukkitRunnable

    init {
        timer = object : BukkitRunnable() {
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
    }


}