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
import pw.valaria.aperture.toRemainingString
import java.time.Duration

class CombatTagEntry(val player: Player, duration: Duration) {

    constructor(player: Player, duration: Long) : this(player, Duration.ofMillis(duration))


    private val key = NamespacedKey(JavaPlugin.getProvidingPlugin(this.javaClass), "ct-${player.name}")
    private var created: Long = -1

    private var expiry: Long = -1
    private var bossBar: BossBar? = null
    private var removed = false

    var duration = duration
        private set

    init {
        bump(duration)
        update()
    }


    fun getRemainingDuration(): Duration {
        return Duration.ofMillis(expiry - System.currentTimeMillis())
    }


    @Synchronized
    fun remove() {
        removed = true
    }

    fun isActive(): Boolean {
        if (removed) return false

        val remaining = getRemainingDuration()
        // if expired, return false and clean above
        if (remaining.isZero || remaining.isNegative) {
            return false
        }

        return true
    }

    @Synchronized
    fun update(): Boolean {
        val remaining = getRemainingDuration()

        if (!isActive()) {
            if (bossBar != null) {
                bossBar!!.removePlayer(this.player)
                bossBar = null
            }
            return false // Allow mid tick removal
        }

        val multi = 1 / duration.toMillis().toDouble()
        val progress = multi * remaining.toMillis()
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(key, "Combat tagged!", BarColor.RED, BarStyle.SEGMENTED_12)
            bossBar!!.addPlayer(player)
        }
        bossBar!!.progress = 1 - progress

        bossBar!!.setTitle("${ChatColor.RED}combat tagged: ${ChatColor.BLUE}${remaining.toRemainingString()}")

        return true

    }

    @Synchronized
    fun bump(duration: Duration) {
        created = System.currentTimeMillis()
        expiry = created + duration.toMillis()
    }


}
