/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.formatter.implementations

import com.google.inject.Inject
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.append
import im.alphhe.alphheimplugin.components.UserManager
import im.alphhe.alphheimplugin.components.chat.ChatChannel
import im.alphhe.alphheimplugin.components.chat.formatter.AbstractChatFormatter
import im.alphhe.alphheimplugin.modules.VaultModule
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class ChatNickFormatter(channel: ChatChannel, plugin: AlphheimCore) : AbstractChatFormatter(channel, plugin) {

    @Inject
    lateinit var vaultModule: VaultModule

    @Inject
    lateinit var userManager: UserManager

    override fun process(sender: Player, message: String, components: ComponentBuilder) {
        val prefix: String? = vaultModule.chat?.getPlayerPrefix(sender);
        val suffix: String? = vaultModule.chat?.getPlayerSuffix(sender);

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
