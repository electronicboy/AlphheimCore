/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat

import im.alphhe.alphheimplugin.components.chat.formatter.IChatFormatter
import im.alphhe.alphheimplugin.data.AlphheimUser
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.entity.Player
import java.util.*

abstract class ChatChannel(val name: String, val chatColor: ChatColor, val shortName: String, @Suppress("UNUSED_PARAMETER") chatHandler: ChatHandlerService) {

    var chatFormatter = LinkedList<IChatFormatter>()
    val chatMembers = HashMap<AlphheimUser, ChatStatus>()

    fun processChat(sender: Player, message: String) {
        val components = ComponentBuilder("")
        chatFormatter.forEach { it.process(sender, message, components) }


    }

    abstract fun canJoin(user: AlphheimUser): Boolean
    abstract fun canLeave(user: AlphheimUser): Boolean
    abstract fun join(user: AlphheimUser, force: Boolean = false): Boolean
    abstract fun leave(user: AlphheimUser, force: Boolean = false): Boolean

    abstract fun sendChat(components: ComponentBuilder)


}