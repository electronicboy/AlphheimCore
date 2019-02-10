/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.nicks.command

import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.addComponent
import pw.valaria.aperture.commands.CoreCommand
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.utils.MessageUtil
import pw.valaria.aperture.utils.MySQL
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@CommandAlias("nick|nickname")
class CommandNick(private val plugin: ApertureCore) : CoreCommand(plugin) {

    @Subcommand("list")
    @CommandPermission("alphheim.mod")
    @Description("list pending nick requests")
    fun list(sender: CommandSender) {
        MySQL.executor.execute {

            MySQL.getConnection().use { conn ->
                val stmt = conn.prepareStatement("SELECT PLAYER_UUID, REQUESTED, STATUS, NICKNAME FROM player_data INNER JOIN player_nicks pn on player_data.PLAYER_ID = pn.PLAYER_ID WHERE pn.REQUESTED IS NOT NULL AND pn.STATUS = 0")

                stmt.use { statement ->
                    val rs = statement.executeQuery()
                    rs.use {

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

                            val groups = plugin.componentHandler.getComponent(PermissionHandler::class.java)!!.getOwnGroupsForOfflineUser(playerUuid).joinToString { group -> group.name }

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
                            deny.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nick reject $userName")

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

                            if (sender is Player) {
                                sender.spigot().sendMessage(*builder.create())
                            } else {
                                sender.sendMessage(TextComponent.toLegacyText(*builder.create()))
                            }


                        }


                    }
                }

            }
        }
    }

    @Subcommand("set")
    @Description("set the nickname for a player")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players")
    fun set(sender: CommandSender, target: OfflinePlayer, nick: String) {
        val uTarget = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(target.uniqueId)
        uTarget.setNickname(nick)
        MessageUtil.sendInfo(sender, "You have set the nickname of ${uTarget.getOfflinePlayer().name} to ${ChatColor.translateAlternateColorCodes('&', nick)}")
    }


    @Subcommand("accept")
    @Description("accept pending nick request")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players")
    fun accept(sender: CommandSender, target: OfflinePlayer) {
        val uTarget = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(target.uniqueId)

        MySQL.executor.execute {
            MySQL.getConnection().use {
                it.prepareStatement("SELECT REQUESTED FROM player_nicks WHERE PLAYER_ID = ? AND REQUESTED IS NOT NULL").use {
                    it.setInt(1, uTarget.userID)
                    val rs = it.executeQuery()
                    rs.use {
                        if (it.next()) {
                            val nick = it.getString("REQUESTED")
                            uTarget.setNickname(nick)
                            MessageUtil.broadcast("alphheim.mod", "${sender.name} has accepted ${uTarget.getOfflinePlayer().name}'s nickname")
                        } else {
                            MessageUtil.sendError(sender, "The user did not have a pending request")
                        }
                    }
                }

            }

        }
    }

    @Subcommand("reject")
    @Description("reject pending nick request")
    @CommandPermission("alphheim.mod")
    @CommandCompletion("@players")
    fun reject(sender: CommandSender, target: OfflinePlayer) {
        val uTarget = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(target.uniqueId)

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
                                            MessageUtil.broadcast("alphheim.mod", "${sender.name} has declined ${uTarget.getOfflinePlayer().name}'s nickname")
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
    @Description("request a nickname")
    fun request(sender: Player, request: String) {
        val requested = request.trim().replace(' ', '_')
        MySQL.executor.execute {
            val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(sender.uniqueId)
            MySQL.getConnection().use { conn ->
                val stmt = conn.prepareStatement("INSERT INTO player_nicks (PLAYER_ID, STATUS, REQUESTED) VALUE (?, 0, ?) ON DUPLICATE KEY UPDATE REQUESTED = VALUES(REQUESTED), STATUS = 0")
                stmt.use {
                    it.setInt(1, user.userID)
                    it.setString(2, requested) // B

                    if (it.executeUpdate() != 0) {
                        MessageUtil.sendInfo(sender, "Your nickname has been requested!")
                        MessageUtil.broadcast("alphheim.mod", "${sender.name} has requested the nickname ${ChatColor.translateAlternateColorCodes('&', requested)}")
                    }
                }
            }
        }
    }

    @Subcommand("status")
    @Description("check the status of your nickname request")
    fun status(sender: Player) {
        MySQL.executor.execute {
            val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(sender.uniqueId)
            MySQL.getConnection().use { conn ->
                conn.prepareStatement("SELECT STATUS, REQUESTED FROM player_nicks WHERE PLAYER_ID = ? AND REQUESTED IS NOT NULL").use { stmt ->
                    stmt.setInt(1, user.userID)
                    stmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            val status = rs.getInt("STATUS")
                            if (status == 0) {
                                MessageUtil.sendInfo(sender, "Your nickname is currently pending!")
                            } else if (status == 1) {
                                MessageUtil.sendError(sender, "Your nickname request has been approved!")
                            } else if (status == 2) {
                                MessageUtil.sendInfo(sender, "your nickname request has been approved!")
                            }
                        } else {
                            MessageUtil.sendError(sender, "You do not have a pending request")
                        }
                    }
                }
            }
        }
    }

    @Subcommand("reset")
    @Description("remove a players nickname")
    @CommandCompletion("@players")
    @CommandPermission("alphheim.mod")
    fun reset(sender: CommandSender, target: OfflinePlayer) {
        val uTarget = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(target.uniqueId)
        uTarget.setNickname(null)
        MessageUtil.sendInfo(sender, "You have reset the nickname of ${target.name}")
    }


    @HelpCommand
    fun unknownCommand(help: CommandHelp) {
        help.showHelp()
    }
}