/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.chests

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.chests.storage.IChestStorage
import pw.valaria.aperture.components.chests.storage.MysqlChestStorage
import pw.valaria.aperture.components.permissions.PermissionHandler
import java.util.*

class ChestHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    public val chestStorage: IChestStorage;

    init {
        chestStorage = MysqlChestStorage(plugin);
        plugin.commandManager.registerCommand(ChestCommands(plugin, this), true)
    }


    override fun onDisable() {

    }



    fun getChestLimitForUser(uuid: UUID): Long {
        val permHandler = plugin.componentHandler.getComponentOrThrow(PermissionHandler::class.java)

        return permHandler.getLongMeta(uuid, "chest_limit", 0)

    }
}