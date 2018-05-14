/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.commands

import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import org.bukkit.entity.Player

class CommandCraft(private val plugin: AlphheimCore) : AlphheimCommand(plugin, "craft") {

    @Default
    @CommandPermission("alphheim.craft")
    fun craft(sender: Player) {
        sender.openWorkbench(null, true)
    }

}