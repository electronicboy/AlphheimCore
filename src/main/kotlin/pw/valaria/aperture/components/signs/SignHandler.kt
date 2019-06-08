/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.signs

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.signs.listeners.SignListener
import pw.valaria.aperture.components.signs.provider.AbstractSign
import pw.valaria.aperture.components.signs.provider.BuySign
import pw.valaria.aperture.components.signs.provider.RankSign
import pw.valaria.aperture.components.signs.provider.SellSign

class SignHandler(plugin: ApertureCore) : AbstractHandler(plugin) {
    val signKey = NamespacedKey(plugin, "signProvider")
    val signTypeKey = NamespacedKey(plugin, "signType")
    val signProviders = HashMap<String, AbstractSign>()

    init {
        RankSign(this).let {
            signProviders.put(it.providerName, it)
        }
        BuySign(this).let {
            signProviders.put(it.providerName, it)
        }
        SellSign(this).let {
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

    fun create(player: Player, sign: Sign, lines: Array<String>) {

        var topLine = lines[0]
        if (topLine.startsWith("[") && topLine.endsWith("]")) {
            topLine = topLine.subSequence(1, topLine.length - 1).toString()
        }

        val handler = signProviders[topLine]

        if (handler != null) {
            handler.create(player, sign, lines.toList())
            object : BukkitRunnable() {
                override fun run() {
                    val nsign = sign.block.state as? Sign
                    if (nsign != null) {
                        handler.render(nsign).let {
                            Bukkit.getOnlinePlayers().forEach { p ->
                                if (p.location.distanceSquared(nsign.location) < 100) {
                                    p.sendSignChange(nsign.location, it.toTypedArray())
                                }
                            }
                        }
                    }
                }
            }.runTaskLater(this.plugin, 5L)
        }

    }
}