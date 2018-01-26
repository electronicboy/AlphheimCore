/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin

import com.google.inject.Module
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import pw.alphheim.alphheimplugin.components.chat.ChatHandlerService
import pw.alphheim.api.services.Chat
import pw.alphheim.services.CraftInternalServicesManager

class AlphheimCore : JavaPlugin(), Module {

    lateinit var chatHandler: ChatHandlerService


    override fun onEnable() {
        val servicesManager = Bukkit.getInternalServices() as CraftInternalServicesManager

        val injector = AlphheimModule(this).createInjector()
        injector.injectMembers(this)


        chatHandler = ChatHandlerService(this)
        servicesManager.registerService(Chat::class.java, chatHandler, this,true)

    }


    override fun onDisable() {
        Bukkit.getInternalServices().unregisterService(Chat::class.java, chatHandler)
        Bukkit.getInternalServices().unregisterServices(this)
    }


}