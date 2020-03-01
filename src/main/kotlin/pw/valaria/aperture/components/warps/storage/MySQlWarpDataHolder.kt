package pw.valaria.aperture.components.warps.storage

import org.bukkit.Location
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.utils.MySQL
import java.sql.SQLException
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class MySQlWarpDataHolder(val plugin: ApertureCore) : WarpDataHolder() {

    override fun createWarp(name: String, target: Location, restricted: Boolean): CompletableFuture<TransactionResult> {
        return CompletableFuture.supplyAsync(Supplier<TransactionResult> {
            MySQL.getConnection().use { conn ->

                conn.prepareStatement("SELECT WARP_NAME FROM warps WHERE WARP_SERVER = ? AND WARP_NAME = ?").use {stmt ->
                    stmt.setString(0, plugin.getInstanceName())
                    stmt.setString(1, name.toLowerCase())
                    stmt.executeQuery().use {rs ->
                        if (rs.next()) {
                            return@Supplier TransactionResult.Failure("warp already exists!")
                        }
                    }
                }

                conn.prepareStatement("INSERT INTO warps (WARP_SERVER, WARP_NAME, WORLD_NAME, WORLD_UUID, WARP_X, WARP_Y, WARP_Z, WARP_PITCH, WARP_YAW) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
                        .use {stmt ->
                            stmt.setString(0, plugin.getInstanceName())
                            stmt.setString(1, name.toLowerCase())
                            stmt.setString(2, target.world.name)
                            stmt.setString(3, target.world.uid.toString())
                            stmt.setDouble(4, target.x)
                            stmt.setDouble(5, target.y)
                            stmt.setDouble(6, target.z)
                            stmt.setFloat(8, target.pitch)
                            stmt.setFloat(9, target.yaw)
                            try {
                                return@Supplier if (stmt.executeUpdate() != 0) { TransactionResult.Success } else { TransactionResult.Failure("failed to commit")}
                            } catch (ex: SQLException) {
                                ex.printStackTrace()
                                return@Supplier TransactionResult.Failure("An internal exception occured!")
                            }
                        }
            }
        }, MySQL.executor)
    }

    override fun deleteWarp(name: String): CompletableFuture<TransactionResult> {
        return CompletableFuture.supplyAsync(Supplier<TransactionResult> {
            MySQL.getConnection().use { conn ->
                conn.use {
                    conn.prepareStatement("DELETE FROM warps WHERE WARP_SERVER = ? AND WARP_NAME = ?").use {stmt ->
                        stmt.setString(0, plugin.getInstanceName())
                        stmt.setString(1, name.toLowerCase())
                        val result = stmt.executeUpdate()
                        if (result == 0) {
                            return@Supplier TransactionResult.Failure("warp does not exist?")
                        } else {
                            return@Supplier TransactionResult.Success
                        }
                    }
                }
            }
        }, MySQL.executor)
    }

}
