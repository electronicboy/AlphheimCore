/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat

import com.google.inject.Inject
import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import im.alphhe.alphheimplugin.data.AlphheimUser
import org.bukkit.entity.Player


class ChatHandlerService(eladriaCore: EladriaCore) : AbstractHandler(eladriaCore),  pw.valaria.api.services.Chat {

    @Inject
    lateinit var userManager: UserManager
    val channels: Map<String, ChatChannel> = mutableMapOf()
    val users: Set<AlphheimUser> = linkedSetOf()
    //val tempChannel = GlobalChatChannel("OOC", ChatColor.RED, "OOC")
    //val tempChannel = ChatChannel()

    init {
        eladriaCore.injector.injectMembers(this)
    }


    public fun addUser(player: Player) {
        val user = userManager.getUser(player)
        channels.forEach { _, chan ->
            if (chan.canJoin(user)) {
                chan.join(user)
            }
        }

    }

    override fun process(sender: Player, message: String) {

    }

    override fun onDisable() {}
}
