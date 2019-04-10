/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.nicks

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.nicks.command.CommandNick
import pw.valaria.aperture.components.nicks.listeners.NickListener


class NickManager(plugin: ApertureCore) : AbstractHandler(plugin) {

    init {
        CommandNick(plugin)
        NickListener(plugin)
    }

}