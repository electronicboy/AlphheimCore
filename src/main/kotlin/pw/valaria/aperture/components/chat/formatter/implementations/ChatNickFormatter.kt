/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.formatter.implementations

import net.kyori.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chat.ChatChannel
import pw.valaria.aperture.components.chat.formatter.AbstractChatFormatter
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.usermanagement.UserManager

class ChatNickFormatter(channel: ChatChannel, plugin: ApertureCore) : AbstractChatFormatter(channel, plugin) {
    override fun process(sender: Player, message: String, components: net.kyori.text.TextComponent.Builder) {
        val prefix: String? = permissionHandler.getPlayerPrefix(sender)
        val suffix: String? = permissionHandler.getPlayerSuffix(sender)

        if (prefix != null) {
            components.append(LegacyComponentSerializer.INSTANCE.deserialize(prefix))
        }

        val user = userManager.getUser(sender)
        // Just because I'd rather not have null show in chat...
        val userName = user.getNickname()

        components.append(LegacyComponentSerializer.INSTANCE.deserialize(userName))

        if (suffix != null) {
            components.append(LegacyComponentSerializer.INSTANCE.deserialize(suffix))
        }
    }

    private val permissionHandler = plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java)
    private val userManager = plugin.componentHandler.getComponentOrThrow(UserManager::class.java)
}
