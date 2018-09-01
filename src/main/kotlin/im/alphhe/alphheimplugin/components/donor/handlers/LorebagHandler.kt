/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.donor.handlers

import net.minecraft.server.v1_13_R2.NBTTagCompound
import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Dye
import java.util.*

class LorebagHandler() : IDonorHandler() {
    override val name: String
        get() = "lorebag"

    override fun handle(player: Player, args: Map<String, String>) {
        val items = player.inventory.addItem(getItemStack(player, args))
        for (item in items) {
            player.location.world.dropItemNaturally(player.location, item.value)
        }

    }

    private fun getItemStack(player: Player, args: Map<String, String>): ItemStack {
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

    }

}