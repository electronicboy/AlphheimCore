/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.data

import org.bukkit.ChatColor

enum class DonorTier(@Suppress("UNUSED_PARAMETER") level: Int, val color: ChatColor) {

    RUBY(1, ChatColor.DARK_RED),
    TOPAZ(2, ChatColor.GOLD),
    EMERALD(3, ChatColor.DARK_GREEN),
    AMETHYST(4, ChatColor.DARK_PURPLE),
    SAPPHIRE(5, ChatColor.BLUE);


}