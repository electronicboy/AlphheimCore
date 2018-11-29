/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.worldgen

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import java.util.*

class EmptyWorldGenerator : ChunkGenerator() {

    override fun generateChunkData(world: World, random: Random, chunkX: Int, chunkZ: Int, biome: BiomeGrid): ChunkData {
        val chunkData = createChunkData(world)
        chunkData.setRegion(0, 0, 0, 15, chunkData.maxHeight, 15, Material.AIR.createBlockData())
        val spawn = world.spawnLocation

        if (chunkX == (spawn.blockX shr 4) && chunkZ == (spawn.blockZ shr 4)) {
            val x = (spawn.blockX and 15)
            val z = (spawn.blockZ and 15)

            val y = if (spawn.y <= 0) {
                (spawn.blockY - 1)
            } else 0

            chunkData.setBlock(x, y, z, Material.BEDROCK.createBlockData())
        }
        return chunkData
    }

}