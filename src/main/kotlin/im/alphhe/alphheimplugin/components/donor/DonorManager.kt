/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor

import co.aikar.commands.InvalidCommandArgument
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.donor.commands.DonorCommand
import im.alphhe.alphheimplugin.components.donor.handlers.IDonorHandler
import im.alphhe.alphheimplugin.components.donor.handlers.LorebagHandler
import im.alphhe.alphheimplugin.components.donor.handlers.MobSpawnerHandler
import im.alphhe.alphheimplugin.components.donor.handlers.ReasonToLiveHandler

class DonorManager(private val plugin: AlphheimCore) {

    private val handlers = mutableMapOf<String, IDonorHandler>()

    init {

        plugin.commandManager.commandContexts.registerContext(IDonorHandler::class.java, {c ->
            val type = c.popFirstArg().toLowerCase()
            getHandler(type) ?: throw InvalidCommandArgument("The $type handler does not exist!")
        })

        plugin.commandManager.commandCompletions.registerCompletion("donorhandler", {c ->
            handlers.keys
        })

        plugin.commandManager.registerCommand(DonorCommand(plugin, this), true)

        registerHandler("spawner", MobSpawnerHandler())
        registerHandler("lorebag", LorebagHandler())
        registerHandler("reasontolive", ReasonToLiveHandler())

    }



    fun getHandler(perk: String): IDonorHandler? {
        return handlers[perk.toLowerCase()]
    }

    fun registerHandler(perk: String, handler: IDonorHandler) {
        handlers[perk.toLowerCase()] = handler
    }


}