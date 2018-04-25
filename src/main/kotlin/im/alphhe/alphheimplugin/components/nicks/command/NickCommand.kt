/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.nicks.command

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class NickCommand(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "anick") {

    @Subcommand("list")
    @CommandPermission("alphheim.mod")
    fun list(sender: Player) {
        MySQL.getConnection().use {
            val stmt = it.prepareStatement("SELECT PLAYER_UUID, REQUESTED, REJECTED, NICKNAME FROM player_data INNER JOIN player_nicks pn on player_data.PLAYER_ID = pn.PLAYER_ID WHERE pn.REQUESTED IS NOT NULL AND pn.REJECTED = 0")

            stmt.use {
                val rs = it.executeQuery()
                rs.use {
                    val count = rs.fetchSize
                    val c = if (count == 0) {
                        "no"
                    } else {
                        count.toString()
                    }

                    MessageUtil.sendInfo(sender, "There are $c pending requests!")

                    while (rs.next()) {
                        val nickRequest = rs.getString("REQUESTED")
                        val playerUuid = UUID.fromString(rs.getString("PLAYER_UUID"))
                        val userName = Bukkit.getOfflinePlayer(playerUuid).name

                        val nick = TextComponent(nickRequest)
                        nick.color = ChatColor.RED

                        val request = TextComponent(" requested by: ")
                        request.color = ChatColor.DARK_RED

                        //val playerData = TextComponent("$userName ($group) ")
                        //playerData.color = ChatColor.RED

                        val openBracket = TextComponent("[")
                        openBracket.color = ChatColor.DARK_RED


                        val accept = TextComponent("✓")
                        accept.isBold = true
                        accept.color = ChatColor.GREEN
                        accept.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick accept $userName")

                        val separator = TextComponent("|")
                        separator.color = ChatColor.DARK_RED

                        // ✗ ✘
                        val deny = TextComponent("✗")
                        deny.isBold = true
                        deny.color = ChatColor.GREEN
                        deny.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick deny $userName")

                        val closeBracket = TextComponent("]")
                        closeBracket.color = ChatColor.DARK_RED


                        nick.addExtra(request)
                        //nick.addExtra(playerData)
                        nick.addExtra(openBracket)
                        nick.addExtra(accept)
                        nick.addExtra(separator)
                        nick.addExtra(deny)
                        nick.addExtra(closeBracket)

                        sender.spigot().sendMessage(nick)


                    }


                }
            }

        }
    }

    @Subcommand("set")
    @CommandPermission("alphheim.mod")
    fun set(sender: Player, target: OfflinePlayer, nick: String) {
        val uTarget = plugin.userManager.getUser(target.uniqueId)
        uTarget.setNickname(nick)
    }


    @Subcommand("accept")
    @CommandPermission("alphheim.mod")
    fun accept(sender: Player, target: OfflinePlayer) {
        val uTarget = plugin.userManager.getUser(target.uniqueId)

        MySQL.executor.execute {
            MySQL.getConnection().use {
                it.prepareStatement("SELECT REQUESTED FROM player_nicks WHERE PLAYER_ID = ? AND REQUESTED IS NOT NULL").use {
                    it.setInt(1, uTarget.userID)
                    val rs = it.executeQuery()
                    rs.use {
                        if (it.next()) {
                            val nick = it.getString("REQUESTED")
                            uTarget.setNickname(nick)
                            MessageUtil.sendInfo(sender, "You have accepted ${uTarget.getOfflinePlayer().name}'s nickname")
                        } else {
                            MessageUtil.sendError(sender, "The user did not have a pending request")
                        }
                    }
                }

            }

        }
    }

    @Subcommand("request")
    fun request(sender: Player, request: String) {
        MySQL.executor.execute {

            val user = plugin.userManager.getUser(sender.uniqueId)
            MySQL.getConnection().use { conn ->
                val stmt = conn.prepareStatement("INSERT INTO player_nicks (PLAYER_ID, REJECTED, REQUESTED) VALUE (?, 0, ?) ON DUPLICATE KEY UPDATE REQUESTED = REQUESTED, REJECTED = 0")
                stmt.use {
                    it.setInt(1, user.userID)
                    it.setString(2, request)

                    if (it.executeUpdate() != 0) {
                        MessageUtil.sendInfo(sender, "Your nickname has been requested!")
                    }
                }
            }
        }
    }


}
