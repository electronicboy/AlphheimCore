/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin.components.chat.formatter

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player

@FunctionalInterface
interface IChatFormatter {

    fun process(sender: Player, message: String, components: ComponentBuilder)
}