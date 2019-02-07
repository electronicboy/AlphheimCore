/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor

import co.aikar.commands.InvalidCommandArgument
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.donor.commands.CommandCraft
import pw.valaria.aperture.components.donor.commands.CommandDonor
import pw.valaria.aperture.components.donor.commands.CommandHat
import pw.valaria.aperture.components.donor.handlers.IDonorHandler
import pw.valaria.aperture.components.donor.handlers.LorebagHandler
import pw.valaria.aperture.components.donor.handlers.MobSpawnerHandler
import pw.valaria.aperture.components.donor.handlers.ReasonToLiveHandler

class DonorManager(plugin: ApertureCore) : AbstractHandler(plugin){

    private val handlers = mutableMapOf<String, IDonorHandler>()

    init {

        plugin.commandManager.commandContexts.registerContext(IDonorHandler::class.java, { c ->
            val type = c.popFirstArg().toLowerCase()
            getHandler(type) ?: throw InvalidCommandArgument("The $type handler does not exist!")
        })

        plugin.commandManager.commandCompletions.registerCompletion("donorhandler", { _ ->
            handlers.keys
        })

        CommandDonor(plugin, this)

        CommandCraft(plugin)
        CommandHat(plugin)

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