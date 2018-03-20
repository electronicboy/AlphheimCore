/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.utils

import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player


object MessageUtil {
    public val default= ChatColor.GREEN
    public val error = ChatColor.DARK_RED
    public val prefix = "${ChatColor.DARK_GRAY}[${ChatColor.RED}A${ChatColor.DARK_GRAY}]"

    fun send(player: Player, s: String) {
        player.sendMessage("$prefix $s")
    }

    fun sendInfo(player: Player, s: String) {
        send(player, "$default$s")
    }

    fun sendError(player: Player, s: String) {
        send(player, "$error$s")
    }

}
