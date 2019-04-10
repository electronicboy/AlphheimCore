/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.channels

import pw.valaria.aperture.components.chat.ChatChannel
import pw.valaria.aperture.components.chat.ChatHandlerService
import pw.valaria.aperture.components.chat.ChatStatus
import pw.valaria.aperture.data.AlphheimUser
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
        return true
    }

    override fun canLeave(user: AlphheimUser): Boolean {
        return false
    }

    override fun join(user: AlphheimUser, force: Boolean): Boolean {

        val chatStatus = user.channelData[name] ?: ChatStatus.ACTIVE
        chatMembers[user] = chatStatus

        return true
    }

}
