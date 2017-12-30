/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin.components.chat.formatter.implementations

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player
import pw.alphheim.alphheimplugin.AlphheimCore
import pw.alphheim.alphheimplugin.components.chat.ChatChannel
import pw.alphheim.alphheimplugin.components.chat.formatter.AbstractChatFormatter

class ChatInfoFormatter(private val channel: ChatChannel, private val plugin: AlphheimCore) : AbstractChatFormatter(channel, plugin) {


    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        components.append("[").color(ChatColor.WHITE)
                .append(channel.shortName).color(channel.chatColor)
                .append("]").color(ChatColor.WHITE)
                .append(" ")
    }

}