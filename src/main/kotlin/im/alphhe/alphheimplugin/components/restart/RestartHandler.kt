/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.restart

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.restart.command.CommandRestart
import im.alphhe.alphheimplugin.components.restart.listener.RestartListener

class RestartHandler(plugin: AlphheimCore) : AbstractHandler(plugin) {

    init {
        RestartListener(this)
        CommandRestart(this, plugin)
    }

    fun getStatus(): Boolean {
        val statusString = System.getProperty("restartStatus") ?: return false
        return statusString.toBoolean()
    }

    fun setStatus(newStatus: Boolean) {
        System.setProperty("restartStatus", newStatus.toString())
    }



}