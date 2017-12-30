/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import pw.alphheim.alphheimplugin.components.chat.ChatHandlerService
import pw.alphheim.api.services.Chat
import pw.alphheim.services.CraftInternalServicesManager

class AlphheimCore : JavaPlugin() {

    lateinit var chatHandler: ChatHandlerService


    override fun onEnable() {
        val servicesManager = Bukkit.getInternalServices() as CraftInternalServicesManager

        chatHandler = ChatHandlerService(this)
        servicesManager.registerService(Chat::class.java, chatHandler, true)

    }


    override fun onDisable() {
        Bukkit.getInternalServices().unregisterService(Chat::class.java, chatHandler)
    }


}