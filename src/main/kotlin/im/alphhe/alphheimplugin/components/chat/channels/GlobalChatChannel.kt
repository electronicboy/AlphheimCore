/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.channels

import im.alphhe.alphheimplugin.components.chat.ChatChannel
import im.alphhe.alphheimplugin.components.chat.ChatHandlerService
import im.alphhe.alphheimplugin.components.chat.ChatStatus
import im.alphhe.alphheimplugin.data.AlphheimUser
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder

class GlobalChatChannel(name: String, color: ChatColor, shortName: String, private val chatHandler: ChatHandlerService) : ChatChannel(name, color, shortName, chatHandler) {
    override fun leave(user: AlphheimUser, force: Boolean): Boolean {
        return chatMembers.remove(user) != null
    }

    override fun sendChat(components: ComponentBuilder) {

        val compiledComponents = components.create()
        chatMembers.filter { it.value == ChatStatus.ACTIVE }.forEach { it.key.getOfflinePlayer().player?.sendMessage(*compiledComponents) }

    }


    override fun canJoin(user: AlphheimUser): Boolean {
        return true;
    }

    override fun canLeave(user: AlphheimUser): Boolean {
        return false;
    }

    override fun join(user: AlphheimUser, force: Boolean): Boolean {

        val chatStatus = user.channelData[name] ?: ChatStatus.ACTIVE
        chatMembers[user] = chatStatus

        return true;
    }

}
