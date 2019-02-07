/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat

import com.google.inject.Inject
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.data.AlphheimUser
import org.bukkit.entity.Player


class ChatHandlerService(apertureCore: ApertureCore) : AbstractHandler(apertureCore),  pw.valaria.api.services.Chat {

    @Inject
    var userManager: UserManager = apertureCore.componentHandler.getComponentOrThrow(UserManager::class.java)
    val channels: Map<String, ChatChannel> = mutableMapOf()
    val users: Set<AlphheimUser> = linkedSetOf()
    //val tempChannel = GlobalChatChannel("OOC", ChatColor.RED, "OOC")
    //val tempChannel = ChatChannel()


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
