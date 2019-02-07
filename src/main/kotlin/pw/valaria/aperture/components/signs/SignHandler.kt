/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.signs

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.signs.data.AbstractSign
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkLoadEvent
import kotlin.reflect.KClass

class SignHandler(plugin: ApertureCore) : AbstractHandler(plugin) {
    val signProviders = HashMap<String, KClass<AbstractSign>>()
    val loadedSigns =  HashMap<Location, AbstractSign>()

    init {


    }

    @EventHandler
    fun chunkLoadEvent(e: ChunkLoadEvent) {

    }
}