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
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ChatMessageFormatter(channel: ChatChannel, plugin: ApertureCore) : AbstractChatFormatter(channel, plugin) {
    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        var msg: String = message

        if (sender.hasPermission("alphheim.chat.color")) {
            msg = ChatColor.translateAlternateColorCodes('&', msg)
        }

        components.append(TextComponent.fromLegacyText(msg))


    }

}