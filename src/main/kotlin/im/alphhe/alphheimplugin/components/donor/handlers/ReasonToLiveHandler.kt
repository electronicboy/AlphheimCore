/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.handlers

import im.alphhe.alphheimplugin.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class ReasonToLiveHandler : IDonorHandler() {
    override fun handle(player: Player, args: Map<String, String>) {
        for (tPlayer in Bukkit.getOnlinePlayers()) {
            MessageUtil.sendInfo(tPlayer, "${player.displayName} has purchased a reason to live!")
        }
    }

}