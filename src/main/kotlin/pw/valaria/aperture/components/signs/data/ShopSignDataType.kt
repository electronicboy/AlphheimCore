/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.data

import com.google.common.io.ByteStreams
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.math.BigDecimal

class ShopSignDataType : PersistentDataType<ByteArray, ShopSignDataType.ShopSignData> {
    private val version = 1
    override fun getPrimitiveType(): Class<ByteArray> = ByteArray::class.java

    override fun getComplexType(): Class<ShopSignData> =ShopSignData::class.java

    override fun toPrimitive(data: ShopSignData, p1: PersistentDataAdapterContext): ByteArray {
        val out = ByteStreams.newDataOutput()

        out.writeInt(version)
        out.writeShort(data.amount.toInt())
        out.writeUTF(data.price.toString())
        out.writeUTF(data.item.key.toString())
        return out.toByteArray()
    }

    override fun fromPrimitive(stream: ByteArray, p1: PersistentDataAdapterContext): ShopSignData {
        val input = ByteStreams.newDataInput(stream)

        val version = input.readInt()
        if (version > this.version) {
            throw IllegalArgumentException("Unsupported data version: $version")
        }
        val amount = input.readShort()
        val price = input.readUTF()
        val materialKey = input.readUTF()

        val priceDecimal = BigDecimal(price)
        val material = Material.matchMaterial(materialKey) ?: Material.AIR

        return ShopSignData(amount, priceDecimal, material)
    }


    data class ShopSignData(var amount: Short, var price: BigDecimal, var item: Material)
}
