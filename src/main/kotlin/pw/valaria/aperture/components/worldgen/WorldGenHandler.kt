/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.worldgen

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import org.bukkit.generator.ChunkGenerator

class WorldGenHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    val emptyWorldGenerator: EmptyWorldGenerator = EmptyWorldGenerator()

    @Suppress("UNUSED_PARAMETER")
    fun getGenerator(worldName: String?, id: String?): ChunkGenerator? {

        return if (id != null) {
            when (id.toLowerCase()) {
                "emptyworldgenerator" -> emptyWorldGenerator
                else ->  null
            }
        } else null

    }

    init {

    }
}