/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.provider

import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pw.valaria.aperture.components.signs.SignHandler
import pw.valaria.aperture.components.signs.data.ShopSignDataType
import pw.valaria.aperture.utils.MessageUtil
import java.math.BigDecimal

abstract class ItemShopSign(handler: SignHandler, providerName: String) : AbstractSign( handler, providerName){

    abstract val header: String

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


        val material: Material = when {
            "hand".equals(materialLine, true) -> player.inventory.itemInMainHand.type
            "offhand".equals(materialLine, true) -> player.inventory.itemInOffHand.type
            else -> Material.matchMaterial(materialLine) ?: Material.AIR
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