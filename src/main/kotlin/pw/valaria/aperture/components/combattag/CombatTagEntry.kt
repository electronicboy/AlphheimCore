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

class CombatTagEntry(val player: Player, duration: Duration) {

    constructor(player: Player, duration: Long) : this(player, Duration.ofMillis(duration))



    private val key = NamespacedKey(JavaPlugin.getProvidingPlugin(this.javaClass), "ct-${player.name}")
    private var created: Long = -1

    private var expiry: Long = -1
    private var bossBar: BossBar
    private var removed = false

    var duration = duration
        private set

    init {
        bump(duration)
        bossBar = Bukkit.createBossBar(key, "Combat tagged!", BarColor.RED, BarStyle.SEGMENTED_12)
        update()
        bossBar.addPlayer(player)
    }


    fun getRemainingDuration(): Duration {
        return Duration.ofMillis(expiry - System.currentTimeMillis())
    }


    fun remove() {
        bossBar.removePlayer(player)
        removed = true
    }

    fun update(): Boolean {
        if (removed) return false // Allow mid tick removal

        val remaining = getRemainingDuration()
        // if expired, return false and clean above
        if (remaining.isZero || remaining.isNegative) {
            return false
        }

        val multi = 1 / duration.toMillis().toDouble()
        val progress = multi * remaining.toMillis()
        bossBar.progress = 1 - progress

        bossBar.title = "${ChatColor.RED}combat tagged: ${ChatColor.BLUE}${remaining.toRemainingString()}"

        return true

    }

    fun bump(duration: Duration) {
        created = System.currentTimeMillis()
        expiry = created + duration.toMillis()
    }


}


private fun Duration.toRemainingString(): String {
    val sb = StringBuilder()

    if (this.toHours() % 24 != 0L) {
        sb.append("${this.toHours() % 24}h")
    }

    if (sb.isNotEmpty() || this.toMinutes() % 60 != 0L) {
        if (sb.isNotEmpty()) sb.append(", ")
        sb.append("${this.toMinutes() % 60}m")
    }
    if (sb.isNotEmpty()) sb.append(", ")
    sb.append("${this.seconds % 60}s")

    if (this.seconds == 0L) {
        sb.append(this.toMillis()).append("ms")
    }

    return sb.toString()
}
