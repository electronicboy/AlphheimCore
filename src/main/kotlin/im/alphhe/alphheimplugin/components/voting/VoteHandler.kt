/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting

import com.sun.istack.internal.logging.Logger
import com.vexsoftware.votifier.Votifier
import com.vexsoftware.votifier.model.VoteListener
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.voting.votelistener.AVoteListener

class VoteHandler(private var plugin: AlphheimCore) {

    private val votifier = plugin.server.pluginManager.getPlugin("Votifier") as? Votifier
    init {

        if (votifier == null) {
            Logger.getLogger(this.javaClass).warning("Could not find votifier plugin!")
        } else {
            votifier.listeners.add(AVoteListener(this))
        }

    }

    fun destruct() {
        votifier?.listeners?.removeIf { it.javaClass == AVoteListener::javaClass }
    }


    fun processVote(username: String, address: String, serviceName: String) {

    }




}