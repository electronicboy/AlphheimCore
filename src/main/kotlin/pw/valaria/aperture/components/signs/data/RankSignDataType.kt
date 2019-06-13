/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.data

import com.google.common.io.ByteStreams
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import kotlin.IllegalArgumentException

class RankSignDataType : PersistentDataType<ByteArray, RankSignDataType.RankSignData> {
    private val version = 1
    companion object {
        @JvmStatic
        val INSTANCE = RankSignDataType()
    }

    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<RankSignData> {
        return RankSignData::class.java
    }

    override fun toPrimitive(rankSignData: RankSignData, p1: PersistentDataAdapterContext): ByteArray {
        @Suppress("UnstableApiUsage") val bytes = ByteStreams.newDataOutput()
        bytes.writeInt(version) // version
        bytes.writeUTF(rankSignData.rank)
        return bytes.toByteArray()
    }

    @Suppress("UnstableApiUsage")
    override fun fromPrimitive(byteArray: ByteArray, p1: PersistentDataAdapterContext): RankSignData {
        val stream = ByteStreams.newDataInput(byteArray)

        val version = stream.readInt()
        if (version > this.version) {
            throw IllegalArgumentException("Unsupported data version: $version")
        }
        val rank = stream.readUTF()

        return RankSignData(rank)
    }


    data class RankSignData(val rank: String)

}