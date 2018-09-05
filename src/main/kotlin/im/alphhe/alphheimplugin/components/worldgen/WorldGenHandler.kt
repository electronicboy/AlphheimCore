/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.worldgen

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import org.bukkit.generator.ChunkGenerator

class WorldGenHandler(plugin: AlphheimCore) : AbstractHandler(plugin) {

    val emptyWorldGenerator: EmptyWorldGenerator = EmptyWorldGenerator()

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