/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.restart

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.restart.command.CommandRestart
import pw.valaria.aperture.components.restart.listener.RestartListener

class RestartHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

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