/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.handlers

import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pw.valaria.aperture.components.donor.DonorManager
import pw.valaria.aperture.components.donor.data.DonationInfo
import pw.valaria.aperture.components.donor.data.DonationInfoTagType

class LorebagHandler(donorManager: DonorManager) : IDonorHandler(donorManager, "lorebag") {

    override fun handle(player: Player, args: Map<String, String>) {
        val items = player.inventory.addItem(getItemStack(player, args))
        for (item in items) {
            player.location.world.dropItemNaturally(player.location, item.value)
        }

    }

    private fun getItemStack(player: Player, args: Map<String, String>): ItemStack {
        val itemstack = ItemStack(Material.LIME_DYE)
        val itemmeta = itemstack.itemMeta

        val transaction = args["transaction"]
        val timestamp = System.currentTimeMillis()
        val purchaser = player.uniqueId

        val donorInfo = DonationInfo(purchaser, timestamp, transaction)
        itemmeta.persistentDataContainer.set(DonationInfoTagType.key, DonationInfoTagType.INSTANCE, donorInfo)

        itemstack.itemMeta = itemmeta

        return itemstack
    }

}