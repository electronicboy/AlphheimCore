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

class BuySign(handler: SignHandler) : AbstractSign(handler, "buy") {
    override fun interact(player: Player, sign: Sign) {
        val ecoReg = handler.plugin.server.servicesManager.getRegistration(Economy::class.java) ?: throw java.lang.IllegalStateException("Vault?!")
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
        }
    }

    val header = "&6[&cBuy&6]".translateColors()

    override fun render(sign: Sign): List<String> {
        val lines = ArrayList<String>()


        val data = sign.persistentDataContainer.get(handler.signKey, ShopSignDataType())
                ?: throw IllegalStateException("Missing sign data?!")

        lines.add(header)
        lines.add(data.amount.toString())
        lines.add(ItemStack(data.item).i18NDisplayName ?: data.item.name.toLowerCase())
        lines.add("$" + data.price)

        return lines

    }

    override fun create(player: Player, sign: Sign, lines: List<String>) {
        if (!player.hasPermission("group.admin")) return
        val amountLine = lines[1]
        val materialLine = lines[2]
        val priceLine = lines[3]


        val amount = amountLine.toShort()
        val price = BigDecimal(priceLine.removePrefix("$"))


        val material: Material = if ("hand".equals(materialLine, true)) {
            player.inventory.itemInMainHand.type
        } else {
            Material.matchMaterial(materialLine) ?: Material.AIR
        }

        if (price < BigDecimal.ZERO) {
            MessageUtil.sendError(player, "Price must be higher than 0!")
            return
        }

        if (amount < 0) {
            MessageUtil.sendError(player, "Amount must be higher than 0!")
            return
        }

        if (material == Material.AIR) {
            MessageUtil.sendError(player, "Unknown item type?")
            return
        }

        val data = ShopSignDataType.ShopSignData(amount, price, material)

        handler.plugin.server.scheduler.runTask(handler.plugin, Runnable {
            val newSign = sign.block.state as? Sign
            if (newSign != null) {
                newSign.persistentDataContainer.set(handler.signTypeKey, PersistentDataType.STRING, providerName)
                newSign.persistentDataContainer.set(handler.signKey, ShopSignDataType(), data)
                newSign.update()
            }
        })

    }
}