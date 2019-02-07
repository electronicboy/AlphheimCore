/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.handlers

import pw.valaria.aperture.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class ReasonToLiveHandler : IDonorHandler() {
    override val name: String
        get() = "ReasonToLive"

    override fun handle(player: Player, args: Map<String, String>) {
        for (tPlayer in Bukkit.getOnlinePlayers()) {
            MessageUtil.sendInfo(tPlayer, "${player.displayName} has purchased a reason to live!")
        }
    }

}