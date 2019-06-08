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
import pw.valaria.aperture.translateColors
import pw.valaria.aperture.utils.MessageUtil
import java.lang.IllegalStateException
import java.math.BigDecimal

class BuyShop(handler: SignHandler) : AbstractSign(handler, "buy") {
    override fun interact(player: Player, sign: Sign) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val header = "&6[&cBuy&6]".translateColors()

    override fun render(sign: Sign): List<String> {
        val lines = ArrayList<String>()


        val data = sign.persistentDataContainer.get(handler.signKey, ShopSignDataType()) ?: throw IllegalStateException("Missing sign data?!")

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

        sign.persistentDataContainer.set(handler.signTypeKey, PersistentDataType.STRING, providerName)
        sign.persistentDataContainer.set(handler.signKey, ShopSignDataType(), data)

    }

}