/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.formatter.implementations

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.append
import im.alphhe.alphheimplugin.components.chat.ChatChannel
import im.alphhe.alphheimplugin.components.chat.formatter.AbstractChatFormatter
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ChatMessageFormatter(channel: ChatChannel, plugin: AlphheimCore) : AbstractChatFormatter(channel, plugin) {
    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        var msg: String = message

        if (sender.hasPermission("alphheim.chat.color")) {
            msg = ChatColor.translateAlternateColorCodes('&', msg)
        }

        components.append(TextComponent.fromLegacyText(msg));


    }

}