/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.permissions

import im.alphhe.alphheimplugin.AlphheimCore
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent

class PermissionHandler(private val plugin: AlphheimCore) {

    init {
        plugin.luckPermsApi.eventBus.subscribe(UserDataRecalculateEvent::class.java, {
            plugin.server.scheduler.runTask(plugin, {
                val player = this.plugin.server.getPlayer(it.user.uuid)
                if (player != null) {
                    plugin.tabListHandler.setSB(player)
                    plugin.healthHandler.updateHealth(player)
                    plugin.racialHandler.applyEffects(player)
                }
            })
        })

    }

    fun destruct() {
        val iter = plugin.luckPermsApi.eventBus.getHandlers(UserDataRecalculateEvent::class.java).iterator();
        while (iter.hasNext()) {
            if (iter.next().consumer.javaClass.classLoader == this.javaClass.classLoader) {
                iter.remove()
            }
        }

    }

}