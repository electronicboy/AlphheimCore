/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.handlers

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import pw.valaria.aperture.components.donor.DonorManager
import pw.valaria.aperture.utils.MessageUtil


class ReasonToLiveHandler(donorManager: DonorManager) : IDonorHandler(donorManager, "ReasonToLive") {

    override fun handle(player: Player, args: Map<String, String>) {
        for (tPlayer in Bukkit.getOnlinePlayers()) {
            MessageUtil.sendInfo(tPlayer, "${player.displayName} has purchased a reason to live!")
        }
    }

}