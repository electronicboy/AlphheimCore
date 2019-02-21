/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.handlers

import org.bukkit.entity.Player
import pw.valaria.aperture.components.donor.DonorManager

abstract class IDonorHandler(val donorManager: DonorManager, val name: String) {

    abstract fun handle(player: Player, args: Map<String, String>)

}
