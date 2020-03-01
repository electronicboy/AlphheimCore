/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.voting.listeners

import com.vexsoftware.votifier.model.VotifierEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import pw.valaria.aperture.components.voting.VoteHandler

class VoteListener(val handler: VoteHandler): Listener {

    init {
        handler.plugin.server.pluginManager.registerEvents(this, handler.plugin)
    }

    @EventHandler
    public fun onVote(e: VotifierEvent) {
        handler.createVote(e.vote.username, e.vote.address, e.vote.serviceName)
    }
}