/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat

import net.kyori.text.TextComponent
import net.kyori.text.format.TextColor
import org.bukkit.entity.Player
import pw.valaria.aperture.components.chat.formatter.IChatFormatter
import pw.valaria.aperture.data.AlphheimUser
import java.util.*

abstract class ChatChannel(val name: String, val chatColor: TextColor, val shortName: String, @Suppress("UNUSED_PARAMETER") chatHandler: ChatHandlerService) {

    var chatFormatter = LinkedList<IChatFormatter>()
    val chatMembers = HashMap<AlphheimUser, ChatStatus>()

    fun processChat(sender: Player, message: String) {
        val components = TextComponent.builder();
        chatFormatter.forEach { it.process(sender, message, components) }


    }

    abstract fun canJoin(user: AlphheimUser): Boolean
    abstract fun canLeave(user: AlphheimUser): Boolean
    abstract fun join(user: AlphheimUser, force: Boolean = false): Boolean
    abstract fun leave(user: AlphheimUser, force: Boolean = false): Boolean

    abstract fun canChat(sender: Player): Boolean
    open fun isDefault(): Boolean {
        return false
    }


}