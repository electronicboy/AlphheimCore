/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.rankcommands.commands.CommandFix
import im.alphhe.alphheimplugin.components.rankcommands.commands.CommandHeal

class RankCommands(plugin: EladriaCore) : AbstractHandler(plugin) {

    init {
        CommandFix(plugin)
        CommandHeal(plugin)
    }

}