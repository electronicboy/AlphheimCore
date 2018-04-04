/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import co.aikar.commands.BukkitCommandManager
import com.google.inject.Injector
import im.alphhe.alphheimplugin.commands.AlphheimCoreCommand
import im.alphhe.alphheimplugin.commands.CommandLore
import im.alphhe.alphheimplugin.components.chat.ChatHandlerService
import im.alphhe.alphheimplugin.components.diversions.FunHandler
import im.alphhe.alphheimplugin.components.motd.MotdHandler
import im.alphhe.alphheimplugin.components.plugincommandperms.PluginCommandPermHandler
import im.alphhe.alphheimplugin.components.spawn.SpawnHandler
import im.alphhe.alphheimplugin.components.tabfooterheader.TabHandler
import im.alphhe.alphheimplugin.components.tablist.TabListHandler
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.java.JavaPlugin

class AlphheimCore : JavaPlugin() {

    lateinit var chatHandler: ChatHandlerService
    lateinit var injector: Injector
        private set
    lateinit var commandManager: BukkitCommandManager
    var commandLore: CommandLore? = null
    var tabHandler: TabHandler? = null
    private lateinit var tabListHandler: TabListHandler


    override fun onEnable() {
        commandManager = BukkitCommandManager(this)
        val commandLore = CommandLore()

        val commandMap = (Bukkit.getCommandMap() as SimpleCommandMap)
        commandMap.getCommand("lore")?.unregister(Bukkit.getCommandMap())

        commandMap.register("lore", "alphheim", commandLore)

        //val servicesManager = Bukkit.getInternalServices() as CraftInternalServicesManager
        injector = AlphheimModule(this).createInjector()
        injector.injectMembers(this)

        registerCommands()
        enableComponents()

        //chatHandler = ChatHandlerService(this)
        //servicesManager.registerService(Chat::class.java, chatHandler, this,true)

    }

    private fun enableComponents() {
        PluginCommandPermHandler(this)
        MotdHandler(this)
        FunHandler(this)
        SpawnHandler(this)
        tabHandler = TabHandler(this)
        tabListHandler = TabListHandler(this);
    }

    private fun registerCommands() {
        AlphheimCoreCommand(this)
    }


    override fun onDisable() {
        //Bukkit.getInternalServices().unregisterService(Chat::class.java, chatHandler)
        Bukkit.getInternalServices().unregisterServices(this)

        if (commandLore != null) commandLore!!.unregister(Bukkit.getCommandMap());
        commandManager.unregisterCommands()

    }


}