/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.voting.rewards

import pw.valaria.aperture.components.voting.VoteHandler
import pw.valaria.aperture.utils.MessageUtil
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