/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.formatter.implementations

import net.kyori.text.serializer.legacy.LegacyComponentSerializer
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chat.ChatChannel
import pw.valaria.aperture.components.chat.formatter.AbstractChatFormatter
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ChatMessageFormatter(channel: ChatChannel, plugin: ApertureCore) : AbstractChatFormatter(channel, plugin) {
    override fun process(sender: Player, message: String, components: net.kyori.text.TextComponent.Builder) {

        val bukkitComponent = ChatColor.valueOf(channel.chatColor.name)

        val component = if (sender.hasPermission("alphheim.chat.color")) {
            LegacyComponentSerializer.INSTANCE.deserialize("&" + bukkitComponent.char + message)
        } else {
            net.kyori.text.TextComponent.of(message).color(channel.chatColor)
        }

        component.append(component)

    }

}