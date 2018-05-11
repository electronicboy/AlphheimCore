/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting

import com.vexsoftware.votifier.Votifier
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.voting.rewards.IVoteReward
import im.alphhe.alphheimplugin.components.voting.votelistener.AVoteListener
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import java.sql.Timestamp

class VoteHandler(internal var plugin: AlphheimCore) {

    private val votifier = plugin.server.pluginManager.getPlugin("Votifier") as? Votifier
    private val rewards: List<IVoteReward> = mutableListOf()

    init {

        if (votifier == null) {
            plugin.logger.warning("Could not find votifier plugin!")
        } else {
            votifier.listeners.add(AVoteListener(this))
            plugin.logger.info("registered vote listener!")
        }

    }

    fun destruct() {
        val removed = votifier?.listeners?.removeIf { it.javaClass == AVoteListener::javaClass }
        if (removed != null && removed) {
            plugin.logger.info("Unregistered vote listener!")
        }
    }


    fun processVote(username: String, address: String, serviceName: String) {
        MessageUtil.broadcast("$username has voted on $serviceName! Remember to vote to support the server and get cool goodies!")
        MySQL.executor.execute({

            val offlinePlayer = plugin.server.getOfflinePlayer(username)
            val user = plugin.userManager.getUser(offlinePlayer.uniqueId)

            MySQL.getConnection().use { conn ->
                conn.prepareStatement(
                        "INSERT INTO player_votes (PLAYER_ID, SERVICE, TIMESTAMP, REDEEMED) VALUE (?, ?, ?, ?)").use { stmt ->
                    stmt.setInt(1, user.userID)
                    stmt.setString(2, address)
                    stmt.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    stmt.setBoolean(4, false)
                    stmt.executeUpdate()
                }


            }

        })


    }


}