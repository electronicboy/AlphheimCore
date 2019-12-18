/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.donor.data

import com.google.common.io.ByteStreams
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * DonationInformationContainer
 *
 * format spec:
 * UUID (long + long) // purchaster
 * timestamp (long) // timestamp
 * boolean (boolean) // contains transaction?
 * String (string) // transaction
 */
class DonationInfoTagType : PersistentDataType<ByteArray, DonationInfo> {

    companion object {
        @Suppress("DEPRECATION")
        @JvmStatic
        val key = NamespacedKey("aperture", "donation_info")


        @JvmStatic
        val INSTANCE = DonationInfoTagType()
    }

    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<DonationInfo> {
        return DonationInfo::class.java
    }

    override fun toPrimitive(donationInfo: DonationInfo, tagAdaptor: PersistentDataAdapterContext): ByteArray {
        @Suppress("UnstableApiUsage") val bytes = ByteStreams.newDataOutput()
        // purchaser - UUID [long + long]
        bytes.writeLong(donationInfo.purchaser.mostSignificantBits)
        bytes.writeLong(donationInfo.purchaser.leastSignificantBits)

        // timestamp - long
        bytes.writeLong(donationInfo.timestamp)

        // transaction - string
        if (donationInfo.transaction != null) {
            bytes.writeBoolean(true)
            bytes.writeUTF(donationInfo.transaction)
        } else {
            bytes.writeBoolean(false)
        }

        bytes.writeBoolean(false) // HAS DATA - force false

        return bytes.toByteArray()

    }

    override fun fromPrimitive(bytes: ByteArray, tagAdaptor: PersistentDataAdapterContext): DonationInfo {
        @Suppress("UnstableApiUsage") val byteStream = ByteStreams.newDataInput(bytes)

        val purchaser = UUID(byteStream.readLong(), byteStream.readLong())
        val timestamp = byteStream.readLong()

        var transaction : String? = null
        if (byteStream.readBoolean()) {
            transaction = byteStream.readUTF()
        }

        val hasCustom = byteStream.readBoolean()
        if (hasCustom) {
            Throwable("Ignoring custom data, we have no idea how to read this: EXPECT DATA LOSS!")
        }

        return DonationInfo(purchaser, timestamp, transaction)
    }

}