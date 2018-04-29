/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.donor.commands.DonorCommand
import im.alphhe.alphheimplugin.components.donor.handlers.IDonorHandler
import im.alphhe.alphheimplugin.components.donor.handlers.LorebagHandler
import im.alphhe.alphheimplugin.components.donor.handlers.MobSpawnerHandler

class DonorManager(private val plugin: AlphheimCore) {

    private val handlers = mutableMapOf<String, IDonorHandler>()

    init {
        plugin.commandManager.registerCommand(DonorCommand(plugin, this), true)

        registerHandler("spawner", MobSpawnerHandler())
        registerHandler("lorebag", LorebagHandler())

    }



    fun getHandler(perk: String): IDonorHandler? {
        return handlers[perk.toLowerCase()]
    }

    fun registerHandler(perk: String, handler: IDonorHandler) {
        handlers[perk.toLowerCase()] = handler
    }


}