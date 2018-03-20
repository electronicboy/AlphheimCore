/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands

import co.aikar.commands.BaseCommand
import im.alphhe.alphheimplugin.AlphheimCore

abstract class AlphheimCommand(plugin: AlphheimCore, name: String) : BaseCommand(name) {
    init {
        plugin.commandManager.registerCommand(this, true)
    }
}
