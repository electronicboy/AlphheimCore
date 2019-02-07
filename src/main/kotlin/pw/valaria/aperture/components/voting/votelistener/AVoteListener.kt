/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.voting.votelistener

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.model.VoteListener
import pw.valaria.aperture.components.voting.VoteHandler

class AVoteListener(private val handler: VoteHandler) : VoteListener {
    override fun voteMade(vote: Vote) {
        handler.createVote(vote.username, vote.address, vote.serviceName)
    }

}