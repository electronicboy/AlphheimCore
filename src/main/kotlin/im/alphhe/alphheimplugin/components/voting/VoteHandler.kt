/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting

import com.vexsoftware.votifier.Votifier
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.voting.rewards.EcoVoteReward
import im.alphhe.alphheimplugin.components.voting.rewards.IVoteReward
import im.alphhe.alphheimplugin.components.voting.rewards.ItemVoteReward
import im.alphhe.alphheimplugin.components.voting.votelistener.AVoteListener
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.v1_8_R3.util.Waitable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.sql.Timestamp
import java.util.concurrent.CompletableFuture
import java.util.concurrent.FutureTask

class VoteHandler(internal var plugin: AlphheimCore) {

    private val votifier = plugin.server.pluginManager.getPlugin("Votifier") as? Votifier
    private val rewards = ArrayList<IVoteReward>()

    init {

        if (votifier == null) {
            plugin.logger.warning("Could not find votifier plugin!")
        } else {
            votifier.listeners.add(AVoteListener(this))
            plugin.logger.info("registered vote listener!")
        }

        rewards.add(EcoVoteReward(this, 2000.0))
        rewards.add(ItemVoteReward(this, ItemStack(Material.DIAMOND, 5)))


    }

    fun destruct() {
        val removed = votifier?.listeners?.removeIf { it.javaClass == AVoteListener::javaClass }
        if (removed != null && removed) {
            plugin.logger.info("Unregistered vote listener!")
        }
    }

    fun createVote(username: String, address: String, serviceName: String) {
        MessageUtil.broadcast("$username has voted on $serviceName! Remember to vote to support the server and get cool goodies!")
        MySQL.executor.execute({

            val offlinePlayer = plugin.server.getOfflinePlayer(username)
            val user = plugin.userManager.getUser(offlinePlayer.uniqueId)
            val task = FutureTask({
                processVote(offlinePlayer)
            } )

            plugin.server.scheduler.runTask(plugin, task)

            val redeemed = task.get()

            MySQL.getConnection().use { conn ->
                conn.prepareStatement(
                        "INSERT INTO player_votes (PLAYER_ID, SERVICE, TIMESTAMP, REDEEMED) VALUE (?, ?, ?, ?)").use { stmt ->
                    stmt.setInt(1, user.userID)
                    stmt.setString(2, address)
                    stmt.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    stmt.setBoolean(4, redeemed)
                    stmt.executeUpdate()
                }

            }

        })


    }


    fun processPlayerLogin(player: Player) {
    }

    fun processVote(player: OfflinePlayer) : Boolean {
        if (!player.isOnline) return false
        val onlinePlayer = player.player ?: return false

        for (voteReward in rewards) {
            try {
                voteReward.process(onlinePlayer)
            } catch (ex: Throwable) {
                plugin.logger.warning("${voteReward.javaClass.canonicalName} thew an exception while processing!")
                ex.printStackTrace()
            }
        }

        return true

    }




}