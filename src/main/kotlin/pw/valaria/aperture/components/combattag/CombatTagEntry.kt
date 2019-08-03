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
import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.toRemainingString
import java.time.Duration
import java.util.concurrent.CompletableFuture

class CombatTagEntry(val player: Player, duration: Duration) {

    private val key = NamespacedKey(JavaPlugin.getProvidingPlugin(this.javaClass), "ct-${player.name}")
    private var created: Long = -1

    private var expiry: Long = -1
    private var bossbarFuture: CompletableFuture<BossBar>? = null
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
        if (!isActive()) {
            if (bossbarFuture != null) {
                val bossBar = bossbarFuture!!.getNow(null) ?: return true
                bossBar.removePlayer(player)
            }
            return false
        }

        if (bossbarFuture == null) {
            if (bossbarFuture == null) {
                bossbarFuture = createFuture(key);
            }
        }

        updateDisplay()
        return true

    }

    private fun updateDisplay() {
        val bossBar = bossbarFuture?.getNow(null) ?: return;

        val remaining = getRemainingDuration()

        val multi = 1 / duration.toMillis().toDouble()
        val progress = multi * remaining.toMillis()
        bossBar.progress = 1 - progress

        if (bossBar.players.isEmpty()) {
            bossBar.addPlayer(player)
        }

        bossBar.setTitle("${ChatColor.RED}combat tagged: ${ChatColor.BLUE}${remaining.toRemainingString()}")
    }

    @Synchronized
    fun bump(duration: Duration) {
        created = System.currentTimeMillis()
        expiry = created + duration.toMillis()
    }

    private fun createFuture(key: NamespacedKey): CompletableFuture<BossBar> {
        val future = CompletableFuture<BossBar>();

        object : BukkitRunnable() {
            override fun run() {
                val bossBar = Bukkit.createBossBar(key, "Combat tagged!", BarColor.RED, BarStyle.SEGMENTED_12)
                future.complete(bossBar)
                this@CombatTagEntry.updateDisplay()
            }

        }.runTask(JavaPlugin.getProvidingPlugin(this.javaClass))

        return future
    }


}
