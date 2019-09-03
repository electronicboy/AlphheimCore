/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.formatter

import net.kyori.text.TextComponent
import org.bukkit.entity.Player

@FunctionalInterface
interface IChatFormatter {

    fun process(sender: Player, message: String, components: TextComponent.Builder)
}