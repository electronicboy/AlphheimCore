/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat

import com.google.inject.Inject
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.UserManager
import im.alphhe.alphheimplugin.data.AlphheimUser
import org.bukkit.entity.Player


class ChatHandlerService(val alphheimCore: AlphheimCore) : pw.alphheim.api.services.Chat {

    @Inject
    lateinit var userManager: UserManager
    val channels: Map<String, ChatChannel> = mutableMapOf()
    val users: Set<AlphheimUser> = linkedSetOf()
    //val tempChannel = GlobalChatChannel("OOC", ChatColor.RED, "OOC")
    //val tempChannel = ChatChannel()

    init {
        alphheimCore.injector.injectMembers(this)
    }


    public fun addUser(player: Player) {
        val user = userManager.getUser(player)
        channels.forEach { name, chan ->
            if (chan.canJoin(player)) {
                chan.join(player)
            }
        }

    }

    override fun process(sender: Player, message: String) {

    }


}