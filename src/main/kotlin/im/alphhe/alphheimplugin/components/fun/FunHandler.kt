/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.`fun`

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.`fun`.listeners.FurnaceListener

class FunHandler(var plugin: AlphheimCore) {

    init {
        plugin.server.pluginManager.registerEvents(FurnaceListener(), plugin)
    }

}