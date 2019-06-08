/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.signs

import org.bukkit.NamespacedKey
import org.bukkit.block.BlockState
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.signs.provider.AbstractSign
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.persistence.PersistentDataType
import pw.valaria.aperture.components.signs.listeners.SignListener
import pw.valaria.aperture.components.signs.provider.RankSign

class SignHandler(plugin: ApertureCore) : AbstractHandler(plugin) {
    val signKey = NamespacedKey(plugin, "signProvider")
    val signTypeKey = NamespacedKey(plugin,  "signType")
    val signProviders = HashMap<String, AbstractSign>()

    init {
        RankSign(this).let {
            signProviders.put(it.providerName, it)
        }

        SignListener(this)
    }

    fun interact(sign: Sign, player: Player) {
        val dataContainer = sign.persistentDataContainer

        val type = dataContainer.get(signTypeKey, PersistentDataType.STRING) ?: return

        val provider = signProviders[type] ?: return

        provider.interact(player, sign)

    }

    fun render(sign: Sign): List<String>? {

        val dataContainer = sign.persistentDataContainer

        val type = dataContainer.get(signTypeKey, PersistentDataType.STRING) ?: return null

        val provider = signProviders[type] ?: return null

        return provider.render(sign)
    }

    fun create(player: Player, sign: Sign) {

    }
}