/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.formatter.implementations

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chat.ChatChannel
import pw.valaria.aperture.components.chat.formatter.AbstractChatFormatter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player

class ChatInfoFormatter(channel: ChatChannel, plugin: ApertureCore) : AbstractChatFormatter(channel, plugin) {


    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        components.append("[").color(ChatColor.WHITE)
                .append(channel.shortName).color(channel.chatColor)
                .append("]").color(ChatColor.WHITE)
                .append(" ")
    }

}