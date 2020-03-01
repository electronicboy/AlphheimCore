package pw.valaria.aperture.components.warps

import com.google.common.cache.CacheBuilder
import org.bukkit.entity.Player
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.warps.data.Warp
import pw.valaria.aperture.components.warps.storage.MySQlWarpDataHolder
import pw.valaria.aperture.components.warps.storage.WarpDataHolder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class WarpHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    val warpData: WarpDataHolder = MySQlWarpDataHolder(plugin);
    val serverWarps = HashMap<String, Warp>()
    val warpCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build<Player, CompletableFuture<List<Warp>>>()




}