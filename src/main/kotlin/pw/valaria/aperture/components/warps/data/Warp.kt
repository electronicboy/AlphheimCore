package pw.valaria.aperture.components.warps.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.lang.IllegalStateException
import java.util.*

class Warp(val name: String, val world: UUID, val x: Double, val y: Double, val z: Double, val restricted: Boolean) {

    fun canAccess(player: Player) = Bukkit.getWorld(world) != null && (!restricted || player.hasPermission("warp.$name"))

    fun toLocation(): Location {
        val bukkitWorld = Bukkit.getWorld(world)
                ?: throw IllegalStateException("world $world is not loaded?")
        return Location(bukkitWorld, x, y, z);

    }
}
