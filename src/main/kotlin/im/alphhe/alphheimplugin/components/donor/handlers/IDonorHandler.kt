/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.handlers

import org.bukkit.entity.Player

abstract class IDonorHandler {
    abstract val name: String

    abstract fun handle(player: Player, args: Map<String, String>)

}
