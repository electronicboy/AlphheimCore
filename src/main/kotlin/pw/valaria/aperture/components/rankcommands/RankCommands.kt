/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.rankcommands

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.rankcommands.commands.CommandFix
import pw.valaria.aperture.components.rankcommands.commands.CommandHeal

class RankCommands(plugin: ApertureCore) : AbstractHandler(plugin) {

    init {
        CommandFix(plugin)
        CommandHeal(plugin)
    }

}