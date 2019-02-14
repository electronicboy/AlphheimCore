/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.combattag

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.time.Duration
import java.util.*
import kotlin.properties.Delegates

class CombatTagEntry(val player: Player, val expiry: Long) {

    private val created = System.currentTimeMillis()
    private val length = Duration.ofMillis(expiry - created)

    private val key = NamespacedKey(JavaPlugin.getProvidingPlugin(this.javaClass), "ct-${player.name}")
    private var bossBar: BossBar

    init {
        bossBar = Bukkit.createBossBar(key, "Combat tagged!", BarColor.RED, BarStyle.SEGMENTED_12)
        bossBar.addPlayer(player)
    }


    fun getDuration(): Duration {
        return Duration.ofMillis(expiry - System.currentTimeMillis())
    }

    fun init() {

    }

    fun remove() {
        bossBar.removePlayer(player)
    }

    fun update(): Boolean {
        val remaining = getDuration()
        // if expired, return false and clean above
        if (remaining.isZero || remaining.isNegative) {
            return false
        }

        val mult = 1 / length.toMillis().toDouble()
        val progress = mult * remaining.toMillis()
        bossBar.progress = 1 - progress

        bossBar.title = "${ChatColor.RED}combat tagged: ${ChatColor.BLUE}${remaining.toRemainingString()}"

        return true

    }


}



private fun Duration.toRemainingString(): String {
    val sb = StringBuilder()

    if (this.toHours() % 24 != 0L) {
        sb.append("${this.toHours() % 24}h")
    }

    if (sb.isNotEmpty() || this.toMinutes() % 60 != 0L) {
        sb.append(", ${this.toMinutes() % 60}m")
    }
    sb.append("${this.seconds}s")

    return sb.toString()
}
