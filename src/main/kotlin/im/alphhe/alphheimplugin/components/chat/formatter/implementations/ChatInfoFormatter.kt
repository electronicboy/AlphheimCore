/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.formatter.implementations

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.chat.ChatChannel
import im.alphhe.alphheimplugin.components.chat.formatter.AbstractChatFormatter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player

class ChatInfoFormatter(private val channel: ChatChannel, plugin: EladriaCore) : AbstractChatFormatter(channel, plugin) {


    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        components.append("[").color(ChatColor.WHITE)
                .append(channel.shortName).color(channel.chatColor)
                .append("]").color(ChatColor.WHITE)
                .append(" ")
    }

}