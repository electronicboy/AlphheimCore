/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.motd

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.motd.commands.CommandMotd
import im.alphhe.alphheimplugin.components.motd.listeners.MotdListener
import org.bukkit.event.Listener

class MotdHandler(val plugin: AlphheimCore) : Listener {

    val listener = MotdListener(this)
    val command = CommandMotd(this)

    init {
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }


}
