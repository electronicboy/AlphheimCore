/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.mmocedits.commands

import co.aikar.commands.annotation.CommandAlias
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.commands.AlphheimCommand
import im.alphhe.alphheimplugin.components.mmocedits.MMOCreditsHandler
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.entity.Player

@CommandAlias("mmocredits")
class CommandCredits(private val plugin: AlphheimCore, private val handler: MMOCreditsHandler) : AlphheimCommand(plugin, "credits"){

    fun creditCheck(sender: Player) {
        handler.getCredits(sender)

    }


}
