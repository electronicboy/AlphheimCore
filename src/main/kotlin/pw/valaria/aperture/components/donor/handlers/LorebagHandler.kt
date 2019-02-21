/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.donor.handlers

import org.bukkit.DyeColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Dye
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
        val itemstack = Dye(DyeColor.LIME).toItemStack(1)
        val itemmeta = itemstack.itemMeta

        //val ret = itemmeta.customTagContainer.getCustomTag(NamespacedKey("core", "donation"), DonationInfoTagType())

        val transaction = args["transaction"]
        val timestamp = System.currentTimeMillis()
        val purchaser = player.uniqueId

        val donorInfo = DonationInfo(purchaser, timestamp, transaction)
        @Suppress("UNCHECKED_CAST") // stupid.
        itemmeta.customTagContainer.setCustomTag(DonationInfoTagType.key, DonationInfoTagType.INSTANCE, donorInfo)

        itemstack.itemMeta = itemmeta

        return itemstack
        /*
        val itemstack = CraftItemStack.asNMSCopy(Dye(DyeColor.LIME).toItemStack(1))

        val tag = itemstack.tag ?: NBTTagCompound()


        if (args["transaction"] != null) {
            tag.setString("transaction", args["transaction"])
        }

        val cal = GregorianCalendar()
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.SECOND, 0)

        tag.setLong("timestamp", cal.timeInMillis )
        tag.setString("purchaser", player.uniqueId.toString())


        itemstack.tag = tag;

        val giveStack = CraftItemStack.asCraftMirror(itemstack)

        val meta = giveStack.itemMeta
        meta.displayName = ChatColor.BOLD.toString() + "Lorebag Token"
        meta.lore = mutableListOf("Speak to staff to redeem")

        giveStack.itemMeta = meta
        return giveStack

         */

    }

}