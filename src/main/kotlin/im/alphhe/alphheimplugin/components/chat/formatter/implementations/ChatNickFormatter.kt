/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.formatter.implementations

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.chat.ChatChannel
import im.alphhe.alphheimplugin.components.chat.formatter.AbstractChatFormatter
import im.alphhe.alphheimplugin.components.permissions.PermissionHandler
import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class ChatNickFormatter(channel: ChatChannel, plugin: EladriaCore) : AbstractChatFormatter(channel, plugin) {

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
