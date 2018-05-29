/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting

import com.vexsoftware.votifier.Votifier
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.voting.rewards.EcoVoteReward
import im.alphhe.alphheimplugin.components.voting.rewards.IVoteReward
import im.alphhe.alphheimplugin.components.voting.rewards.ItemVoteReward
import im.alphhe.alphheimplugin.components.voting.rewards.MMOCreditReward
import im.alphhe.alphheimplugin.components.voting.votelistener.AVoteListener
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.PluginClassLoader
import java.sql.Timestamp
import java.util.concurrent.FutureTask

class VoteHandler(plugin: AlphheimCore) : AbstractHandler(plugin) {

    private val votifier = plugin.server.pluginManager.getPlugin("Votifier") as? Votifier
    private val rewards = ArrayList<IVoteReward>()
    var voteHandler: AVoteListener? = null

    init {

        if (votifier == null) {
            plugin.logger.warning("Could not find votifier plugin!")
        } else {

            votifier.listeners.removeIf({
                val cl = it.javaClass.classLoader as? PluginClassLoader
                cl != null && cl.plugin.name == plugin.name
            })

            voteHandler = AVoteListener(this)
            votifier.listeners.add(voteHandler)
            plugin.logger.info("registered vote listener!")
        }

        rewards.add(EcoVoteReward(this, 2000.0))
        rewards.add(ItemVoteReward(this, ItemStack(Material.DIAMOND, 5)))
        rewards.add(MMOCreditReward(this, 2))


    }

    fun destruct() {
        val removed = votifier?.listeners?.remove(voteHandler)
    }

    fun createVote(username: String, address: String, serviceName: String) {
        MessageUtil.broadcast("$username has voted on $serviceName! Remember to vote to support the server and get cool goodies!")
        MySQL.executor.execute({

            val offlinePlayer = plugin.server.getOfflinePlayer(username)
            val user = plugin.userManager.getUser(offlinePlayer.uniqueId)
            val task = FutureTask({
                processVote(offlinePlayer)
            })

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

        })


    }


    fun processPlayerLogin(player: Player) {
        if (player.name != "electronicboy") return

        MySQL.executor.execute({
            val user = plugin.userManager.getUser(player)
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

                    val task = FutureTask<Boolean>({
                        processVote(player)
                    })
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
        })

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