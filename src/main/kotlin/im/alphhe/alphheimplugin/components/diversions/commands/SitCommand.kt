/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.diversions.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.entity.Player

class SitCommand(var plugin: AlphheimCore ) : BaseCommand("sit") {


    @Default
    fun sit(sender: Player): Unit {
        if (sender.isOp) MessageUtil.sendError(sender, "Go fuck yourself")
        else MessageUtil.sendInfo(sender, "This command coming to an area near you!")
    }

}