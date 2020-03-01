/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.formatter.implementations

import net.kyori.text.TextComponent
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chat.ChatChannel
import pw.valaria.aperture.components.chat.formatter.AbstractChatFormatter

class ChatInfoFormatter(channel: ChatChannel, plugin: ApertureCore) : AbstractChatFormatter(channel, plugin) {
    override fun process(sender: Player, message: String, components: TextComponent.Builder) {

        components
                .append("[" + channel.shortName ?: channel.name + "]").color(channel.chatColor)
    }

}