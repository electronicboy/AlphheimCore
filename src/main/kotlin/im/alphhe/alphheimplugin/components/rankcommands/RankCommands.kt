/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.rankcommands

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.rankcommands.commands.CommandFix

class RankCommands(private val plugin: AlphheimCore) {

    init {
        CommandFix(plugin)
    }

}