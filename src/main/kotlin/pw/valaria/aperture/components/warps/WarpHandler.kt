package pw.valaria.aperture.components.warps

import com.google.common.cache.CacheBuilder
import org.bukkit.Location
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.warps.data.Warp
import pw.valaria.aperture.utils.MySQL
import java.sql.SQLException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

class WarpHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    val serverWarps = HashMap<String, Warp>()
    val warpCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build<Player, CompletableFuture<List<Warp>>>()

    fun createWarp(name: String, target: Location, restricted: Boolean): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync(Supplier<Boolean> {
            MySQL.getConnection().use { conn ->

                conn.prepareStatement("SELECT WARP_NAME FROM warps WHERE WARP_SERVER = ? AND WARP_NAME = ?").use {stmt ->
                    stmt.setString(0, plugin.getInstanceName())
                    stmt.setString(1, name)
                    stmt.executeQuery().use {rs ->
                        if (rs.next()) {
                            return@Supplier false
                        }
                    }
                }


                conn.prepareStatement("INSERT INTO warps (WARP_SERVER, WARP_NAME, WORLD_NAME, WORLD_UUID, WARP_X, WARP_Y, WARP_Z, WARP_PITCH, WARP_YAW) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
                        .use {stmt ->
                            stmt.setString(0, plugin.getInstanceName())
                            stmt.setString(1, name)
                            stmt.setString(2, target.world.name)
                            stmt.setString(3, target.world.uid.toString())
                            stmt.setDouble(4, target.x)
                            stmt.setDouble(5, target.y)
                            stmt.setDouble(6, target.z)
                            stmt.setFloat(8, target.pitch)
                            stmt.setFloat(9, target.yaw)
                            try {
                                return@Supplier stmt.executeUpdate() != 0
                            } catch (ex: SQLException) {
                                return@Supplier false
                            }
                        }
            }
        }, MySQL.executor)
    }

}