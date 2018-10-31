/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.utils

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender


object MessageUtil {
    private val prefix = "${ChatColor.DARK_GRAY}E"
    private val default = "$prefix${ChatColor.GREEN}>"
    private val error = "$prefix${ChatColor.DARK_RED}>"

    private fun send(player: CommandSender, s: String) {
        player.sendMessage("$prefix $s")
    }

    fun sendInfo(player: CommandSender, s: String) {
        send(player, "$default$s")
    }

    fun sendError(player: CommandSender, s: String) {
        send(player, "$error$s")
    }

    fun broadcast(perm: String?, message: String) {
        Bukkit.getConsoleSender().sendMessage(message)
        for (player in Bukkit.getOnlinePlayers()) {
            if (perm == null || player.hasPermission(perm))
                sendInfo(player, message)
        }
    }

    fun broadcast(message: String) {
        broadcast(null, message)
    }

    fun format(prefix: String = "A", color: ChatColor = ChatColor.RED): String {
        return "${ChatColor.DARK_GRAY}[$color$prefix${ChatColor.DARK_GRAY}] "
    }

}
