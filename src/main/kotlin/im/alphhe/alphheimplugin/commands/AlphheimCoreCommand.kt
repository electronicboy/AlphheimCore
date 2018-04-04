/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player

class AlphheimCoreCommand(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "alphheim") {


    @CommandPermission("alphheim.admin")
    @Subcommand("reloadTab")
    fun reloadTab(sender: Player) {
        val tabHandler = plugin.tabHandler
        if (tabHandler == null) {
            MessageUtil.sendError(sender, "TabHandler is null?!")
        } else {
            tabHandler.reset()
        }
    }

}


