/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.provider

import net.milkbowl.vault.economy.Economy
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pw.valaria.aperture.components.signs.SignHandler
import pw.valaria.aperture.components.signs.data.ShopSignDataType
import pw.valaria.aperture.translateColors
import pw.valaria.aperture.utils.MessageUtil
import java.math.BigDecimal

class BuySign(handler: SignHandler) : ShopSign(handler, "buy") {
    override val header = "&6[&cBuy&6]".translateColors()

    override fun interact(player: Player, sign: Sign) {
        val ecoReg = handler.plugin.server.servicesManager.getRegistration(Economy::class.java)
                ?: throw java.lang.IllegalStateException("Vault?!")
        val eco = ecoReg.provider

        val data = sign.persistentDataContainer.get(handler.signKey, ShopSignDataType())
                ?: throw IllegalStateException("Missing sign data?!")

        val hasDosh = eco.has(player, data.price.toDouble())
        if (hasDosh) {
            val itemstack = ItemStack(data.item).apply {
                amount = data.amount.toInt()
            }

            val remaining = player.inventory.addItem(itemstack)

            if (remaining.isNotEmpty()) {
                val lost = data.amount - remaining[0]?.amount!!
                itemstack.amount = lost
                player.inventory.removeItem(itemstack)
                MessageUtil.sendError(player, "You do not have enough space to purchase this item!")
            } else {
                val result = eco.withdrawPlayer(player, data.price.toDouble())
            }
        } else {
            MessageUtil.sendError(player, "You do not have $${data.price}!")

        }
    }




}