/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.provider

import net.milkbowl.vault.economy.Economy
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pw.valaria.aperture.components.signs.SignHandler
import pw.valaria.aperture.components.signs.data.ShopSignDataType
import pw.valaria.aperture.translateColors
import pw.valaria.aperture.utils.MessageUtil

class SellSign(handler: SignHandler) : ItemShopSign(handler, "sell") {
    override val header = "&6[&cSell&6]".translateColors()
    override fun interact(player: Player, sign: Sign) {
        val ecoReg = handler.plugin.server.servicesManager.getRegistration(Economy::class.java) ?: throw java.lang.IllegalStateException("Vault?!")
        val eco = ecoReg.provider

        val data = sign.persistentDataContainer.get(handler.signKey, ShopSignDataType())
                ?: throw IllegalStateException("Missing sign data?!")

        val itemstack = ItemStack(data.item).apply {
            amount = data.amount.toInt()
        }
        if (player.inventory.containsAtLeast(itemstack, itemstack.amount)) {
            player.inventory.removeItemAnySlot(itemstack)
            eco.depositPlayer(player, data.price.toDouble())
        } else {
            MessageUtil.sendError(player, "You do not have ${itemstack.amount} ${itemstack.i18NDisplayName ?: itemstack.type.name.toLowerCase()}")
        }
    }

}