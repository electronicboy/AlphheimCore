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
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class ChatNickFormatter(channel: ChatChannel, plugin: ApertureCore) : AbstractChatFormatter(channel, plugin) {

    private val permissionHandler = plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java)
    private val userManager = plugin.componentHandler.getComponentOrThrow(UserManager::class.java)


    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        val prefix: String? = permissionHandler.getPlayerPrefix(sender)
        val suffix: String? = permissionHandler.getPlayerSuffix(sender)

        if (prefix != null) {
            components.append(TextComponent.fromLegacyText(prefix))
        }

        val user = userManager.getUser(sender)
        // Just because I'd rather not have null show in chat...
        val userName = user.getNickname()

        components.append(TextComponent.fromLegacyText(userName))

        if (suffix != null) {
            components.append(TextComponent.fromLegacyText(suffix))
        }

    }
}
