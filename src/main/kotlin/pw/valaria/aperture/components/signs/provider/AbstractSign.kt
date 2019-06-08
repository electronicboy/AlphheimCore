/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.signs.provider

import org.bukkit.NamespacedKey
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import pw.valaria.aperture.components.signs.SignHandler

abstract class AbstractSign(val handler: SignHandler, public val providerName: String) {

    abstract fun interact(player: Player, sign: Sign)

    abstract fun render(sign: Sign): List<String>

    abstract fun create(player: Player, sign: Sign, lines: List<String>)

    fun getProviderKey() = NamespacedKey(handler.plugin, providerName)
}
