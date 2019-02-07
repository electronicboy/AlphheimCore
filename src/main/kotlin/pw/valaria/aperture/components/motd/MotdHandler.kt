/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.motd

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.motd.commands.CommandMotd
import pw.valaria.aperture.components.motd.listeners.MotdListener

class MotdHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    val listener = MotdListener(this)
    val command = CommandMotd(this)

    init {
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }



}
