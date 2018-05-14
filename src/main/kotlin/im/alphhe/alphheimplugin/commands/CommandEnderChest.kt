/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.entity.Player

@CommandAlias("ec")
class CommandEnderChest(plugin: AlphheimCore) : AlphheimCommand(plugin, "enderchest") {

    @CommandPermission("alphheim.enderchest")
    @Default
    fun enderChest(sender: Player) {
        sender.openInventory(sender.enderChest)
    }
}
