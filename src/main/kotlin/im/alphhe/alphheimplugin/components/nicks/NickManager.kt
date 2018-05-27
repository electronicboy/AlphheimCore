/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.nicks

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.nicks.command.CommandNick
import im.alphhe.alphheimplugin.components.nicks.listeners.NickListener

class NickManager(plugin: AlphheimCore) : AbstractHandler(plugin) {

    init {
        CommandNick(plugin);
        NickListener(plugin)
    }

}