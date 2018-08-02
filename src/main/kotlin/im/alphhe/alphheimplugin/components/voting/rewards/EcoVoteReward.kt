/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.voting.rewards

import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.utils.MessageUtil
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player

import java.text.NumberFormat

class EcoVoteReward(private val voteHandler: VoteHandler, private val amount: Double) : IVoteReward {

    private var ecoService: Economy?


    init {
        if (voteHandler.plugin.server.pluginManager.getPlugin("Vault") == null) {
            throw IllegalStateException("Vault is not loaded?!")
        }

        val registeredServiceProvider = voteHandler.plugin.server.servicesManager.getRegistration(Economy::class.java)
        if (registeredServiceProvider != null) {
            ecoService = registeredServiceProvider.provider
        } else {
            throw IllegalStateException("Eco plugin is not loaded?!")
        }

    }


    override fun process(player: Player): Boolean {
        val response = ecoService!!.depositPlayer(player, amount)
        if (response.transactionSuccess()) {
            val amountPretty = NumberFormat.getCurrencyInstance().format(amount).replace(".00", "")
            MessageUtil.sendInfo(player, "You have received $amountPretty!")
        }

        return true

    }
}