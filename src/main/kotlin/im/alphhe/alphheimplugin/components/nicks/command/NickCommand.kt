/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.nicks.command

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.addComponent
import im.alphhe.alphheimplugin.append
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("nickname")
class NickCommand(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "nick") {

    @Subcommand("list")
    @CommandPermission("alphheim.mod")
    fun list(sender: Player) {
        MySQL.executor.execute {

            MySQL.getConnection().use {
                val stmt = it.prepareStatement("SELECT PLAYER_UUID, REQUESTED, STATUS, NICKNAME FROM player_data INNER JOIN player_nicks pn on player_data.PLAYER_ID = pn.PLAYER_ID WHERE pn.REQUESTED IS NOT NULL AND pn.STATUS = 0")

                stmt.use {
                    val rs = it.executeQuery()
                    rs.use {
                        val count = rs.fetchSize


                        rs.last()
                        val c = if (rs.row == 0) {
                            "no"
                        } else {
                            rs.row.toString()
                        }
                        rs.beforeFirst()
                        MessageUtil.sendInfo(sender, "There are $c pending nick requests!")

                        while (rs.next()) {
                            val nickRequest = rs.getString("REQUESTED")
                            val playerUuid = UUID.fromString(rs.getString("PLAYER_UUID"))
                            val userName = Bukkit.getOfflinePlayer(playerUuid).name
                            val nick = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', nickRequest))

                            val request = TextComponent(" requested by: ")
                            request.color = ChatColor.DARK_RED

                            val groups = plugin.permissionHandler.getOwnGroupsForOfflineUser(playerUuid).joinToString { group -> group.name }

                            val playerData = TextComponent("$userName ($groups) ")
                            playerData.color = ChatColor.RED

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

                            val builder = ComponentBuilder("* ")
                            builder.color(ChatColor.RED)
                            builder.append(nick)
                            builder.addComponent(request)
                            builder.addComponent(playerData)
                            builder.addComponent(openBracket)
                            builder.addComponent(accept)
                            builder.addComponent(separator)
                            builder.addComponent(deny)
                            builder.addComponent(closeBracket)

                            sender.spigot().sendMessage(*builder.create())


                        }


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
        MessageUtil.sendInfo(sender, "You have set the nickname of ${uTarget.getOfflinePlayer().name} to ${ChatColor.translateAlternateColorCodes('&', nick)}")
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

    @Subcommand("reject")
    @CommandPermission("alphheim.mod")
    fun reject(sender: Player, target: OfflinePlayer) {
        val uTarget = plugin.userManager.getUser(target.uniqueId)

        MySQL.executor.execute {
            MySQL.getConnection().use { conn ->
                conn.prepareStatement("SELECT REQUESTED FROM player_nicks WHERE PLAYER_ID = ? AND REQUESTED IS NOT NULL").use {
                    it.setInt(1, uTarget.userID)
                    it.executeQuery().use {
                        if (it.next()) {
                            MySQL.executor.execute {

                                MySQL.getConnection().use { conn ->
                                    conn.prepareStatement("UPDATE player_nicks SET STATUS = 2 WHERE PLAYER_ID = ?").use {
                                        it.setInt(1, uTarget.userID)
                                        if (it.executeUpdate() != 0) {
                                            MessageUtil.sendInfo(sender, "Nickname request for ${uTarget.getOfflinePlayer().name} has been declined!")
                                            val player = uTarget.getOfflinePlayer().player
                                            if (player != null) {
                                                MessageUtil.sendError(player, "Your nickname has been declined!")
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            MessageUtil.sendError(sender, "This user does not have a pending request")
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
                val stmt = conn.prepareStatement("INSERT INTO player_nicks (PLAYER_ID, STATUS, REQUESTED) VALUE (?, 0, ?) ON DUPLICATE KEY UPDATE REQUESTED = VALUES(REQUESTED), STATUS = 0")
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

    @Subcommand("status")
    fun status(sender: Player) {
        MySQL.executor.execute {
            val user = plugin.userManager.getUser(sender.uniqueId)
            MySQL.getConnection().use { conn ->
                conn.prepareStatement("SELECT STATUS, REQUESTED FROM player_nicks WHERE PLAYER_ID = ? AND REQUESTED IS NOT NULL").use { stmt ->
                    stmt.setInt(1, user.userID)
                    stmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            val status = rs.getInt("STATUS")
                            if (status == 0) {
                                MessageUtil.sendInfo(sender, "Your nickname is currently pending!")
                            } else if (status == 1) {
                                MessageUtil.sendError(sender, "Your nickname request has been STATUS!")
                            }

                        } else {
                            MessageUtil.sendError(sender, "You do not have a pending request")
                        }
                    }
                }
            }
        }
    }


}
