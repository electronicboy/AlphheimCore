/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.data

import org.bukkit.ChatColor

fun getById(level: Int): DonorTier {
    return DonorTier.values()[level]
}

enum class DonorTier(@Suppress("UNUSED_PARAMETER") val level: Int, val color: ChatColor) {
    NONE(0, ChatColor.GRAY),
    RUBY(1, ChatColor.DARK_RED),
    TOPAZ(2, ChatColor.GOLD),
    EMERALD(3, ChatColor.DARK_GREEN),
    AMETHYST(4, ChatColor.DARK_PURPLE),
    SAPPHIRE(5, ChatColor.BLUE);


}