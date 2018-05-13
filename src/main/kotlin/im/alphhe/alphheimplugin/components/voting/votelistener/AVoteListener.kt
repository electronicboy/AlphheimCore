/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting.votelistener

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.model.VoteListener
import im.alphhe.alphheimplugin.components.voting.VoteHandler

class AVoteListener(private val handler: VoteHandler) : VoteListener {
    override fun voteMade(vote: Vote) {
        handler.createVote(vote.username, vote.address, vote.serviceName)
    }

}