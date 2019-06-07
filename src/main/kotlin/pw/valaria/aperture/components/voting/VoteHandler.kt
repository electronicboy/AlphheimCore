/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.voting

import com.vexsoftware.votifier.NuVotifierBukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.components.voting.listeners.VoteListener
import pw.valaria.aperture.components.voting.rewards.EcoVoteReward
import pw.valaria.aperture.components.voting.rewards.IVoteReward
import pw.valaria.aperture.components.voting.rewards.ItemVoteReward
import pw.valaria.aperture.components.voting.rewards.MMOCreditReward
import pw.valaria.aperture.utils.MessageUtil
import pw.valaria.aperture.utils.MySQL
import java.sql.Timestamp
import java.util.concurrent.FutureTask

class VoteHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    private val votifier = plugin.server.pluginManager.getPlugin("Votifier") as? NuVotifierBukkit
    private val rewards = ArrayList<IVoteReward>()

    init {

        VoteListener(this)

        rewards.add(EcoVoteReward(this, 2000.0))
        rewards.add(ItemVoteReward(this, ItemStack(Material.DIAMOND, 5)))
        rewards.add(MMOCreditReward(this, 2))


    }

    @Suppress("UNUSED_PARAMETER")
    fun createVote(username: String, address: String, serviceName: String) {
        MessageUtil.broadcast("$username has voted on $serviceName! Remember to vote to support the server and get cool goodies!")
        MySQL.executor.execute {

            val offlinePlayer = plugin.server.getOfflinePlayer(username)
            val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(offlinePlayer.uniqueId)
            val task = FutureTask {
                processVote(offlinePlayer)
            }

            plugin.server.scheduler.runTask(plugin, task)

            val redeemed = task.get()

            MySQL.getConnection().use { conn ->
                conn.prepareStatement(
                        "INSERT INTO player_votes (PLAYER_ID, SERVICE, TIMESTAMP, REDEEMED) VALUE (?, ?, ?, ?)").use { stmt ->
                    stmt.setInt(1, user.userID)
                    stmt.setString(2, serviceName)
                    stmt.setTimestamp(3, Timestamp(System.currentTimeMillis()))
                    stmt.setBoolean(4, redeemed)
                    stmt.executeUpdate()
                }

            }

        }


    }


    fun processPlayerLogin(player: Player) {
        if (player.name != "electronicboy") return

        MySQL.executor.execute {
            val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(player)
            MySQL.getConnection().use { conn ->
                val votes = ArrayList<Int>()

                conn.autoCommit = false // we're about to go barebones again...
                conn.prepareStatement("SELECT VOTE_ID FROM player_votes WHERE PLAYER_ID = ? AND REDEEMED = FALSE FOR UPDATE ").use { stmt ->
                    stmt.setInt(1, user.userID)
                    stmt.executeQuery().use { rs ->
                        while (rs.next()) {
                            votes.add(rs.getInt("VOTE_ID"))
                        }
                    }

                }

                for (voteID in votes) {
                    if (!player.isOnline) break

                    val task = FutureTask<Boolean> {
                        processVote(player)
                    }

                    plugin.server.scheduler.runTask(plugin, task)

                    if (task.get()) {
                        conn.prepareStatement("UPDATE player_votes SET REDEEMED = TRUE WHERE VOTE_ID = ?").use { stmt ->
                            stmt.setInt(1, voteID)
                            stmt.execute()
                        }
                    }


                }

                conn.commit()
                conn.autoCommit = true

            }
        }

    }

    fun processVote(player: OfflinePlayer): Boolean {
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