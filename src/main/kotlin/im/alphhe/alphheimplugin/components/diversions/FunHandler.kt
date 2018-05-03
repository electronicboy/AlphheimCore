/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.diversions

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.diversions.commands.CommandRoulette
import im.alphhe.alphheimplugin.components.diversions.listeners.FurnaceListener

class FunHandler(var plugin: AlphheimCore) {

    init {
        plugin.server.pluginManager.registerEvents(FurnaceListener(), plugin)
        plugin.commandManager.registerCommand(CommandRoulette(plugin))
    }

}