/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.channels

import im.alphhe.alphheimplugin.components.chat.ChatChannel
import im.alphhe.alphheimplugin.components.chat.ChatHandlerService
import im.alphhe.alphheimplugin.components.chat.ChatStatus
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player

class GlobalChatChannel(name: String, color: ChatColor, shortName: String, private val chatHandler: ChatHandlerService) : ChatChannel(name, color, shortName, chatHandler) {
    override fun leave(player: Player, force: Boolean): Boolean {
        return chatMembers.remove(player) != null
    }

    override fun sendChat(components: ComponentBuilder) {

        val compiledComponents = components.create()
        chatMembers.filter { it.value == ChatStatus.ACTIVE }.forEach { it.key.sendMessage(*compiledComponents) }

    }


    override fun canJoin(player: Player): Boolean {
        return true;
    }

    override fun canLeave(player: Player): Boolean {
        return false;
    }

    override fun join(player: Player, force: Boolean): Boolean {
        val user = chatHandler.userManager.getUser(player)

        val chatStatus = user.channelData[name] ?: ChatStatus.ACTIVE
        chatMembers[player] = chatStatus

        return true;
    }

}
