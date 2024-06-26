/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.signs.listeners

import com.destroystokyo.paper.block.SendSignEvent
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import pw.valaria.aperture.components.signs.SignHandler

class SignListener(val handler: SignHandler) : Listener {

    init {
        handler.plugin.server.pluginManager.registerEvents(this, handler.plugin)
    }

    @EventHandler
    fun signInteract(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK || e.hand != EquipmentSlot.HAND) return

        val clicked = e.clickedBlock ?: return

        val sign = clicked.state as? Sign ?: return

        handler.interact(sign, e.player)

    }

    @EventHandler
    fun signSend(e: SendSignEvent) {
        val sign = e.block.state as? Sign ?: return

        handler.render(sign)?.forEachIndexed() { index, line ->
            e.setLine(index, line)
        }
    }

    fun blockBreak(e: BlockBreakEvent) {

    }

    @EventHandler
    fun signUpdate(e: SignChangeEvent) {
        val topLine = e.lines[0]
        if (topLine.startsWith("[") && topLine.endsWith("]")) {

            handler.create(e.player, e.block.state as Sign, e.lines)


        }
    }

}