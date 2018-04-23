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
import im.alphhe.alphheimplugin.components.UserManager
import im.alphhe.alphheimplugin.components.chat.ChatHandlerService
import im.alphhe.alphheimplugin.components.diversions.FunHandler
import im.alphhe.alphheimplugin.components.health.HealthHandler
import im.alphhe.alphheimplugin.components.motd.MotdHandler
import im.alphhe.alphheimplugin.components.permissions.PermissionHandler
import im.alphhe.alphheimplugin.components.plugincommandperms.PluginCommandPermHandler
import im.alphhe.alphheimplugin.components.racial.RacialHandler
import im.alphhe.alphheimplugin.components.rankcommands.RankCommands
import im.alphhe.alphheimplugin.components.spawn.SpawnHandler
import im.alphhe.alphheimplugin.components.tabfooterheader.TabHandler
import im.alphhe.alphheimplugin.components.tablist.TabListHandler
import im.alphhe.alphheimplugin.listeners.PlayerListener
import im.alphhe.alphheimplugin.utils.MySQL
import me.lucko.luckperms.api.LuckPermsApi
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.java.JavaPlugin


class AlphheimCore : JavaPlugin() {

    lateinit var chatHandler: ChatHandlerService
    lateinit var userManager: UserManager
    lateinit var injector: Injector
        private set
    lateinit var commandManager: BukkitCommandManager
    var commandLore: CommandLore? = null
    var tabHandler: TabHandler? = null
    lateinit var tabListHandler: TabListHandler
    lateinit var luckPermsApi: LuckPermsApi
    lateinit var healthHandler: HealthHandler
    lateinit var permissionHandler: PermissionHandler
    lateinit var racialHandler: RacialHandler


    override fun onEnable() {
        MySQL.init(this)

        val provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)
        if (provider != null) {
            luckPermsApi = provider.provider
        }

        userManager = UserManager(this)
        commandManager = BukkitCommandManager(this)
        @Suppress("DEPRECATION")
        commandManager.enableUnstableAPI("help")

        val commandLore = CommandLore()

        val commandMap = (Bukkit.getCommandMap() as SimpleCommandMap)
        commandMap.getCommand("lore")?.unregister(Bukkit.getCommandMap())

        commandMap.register("lore", "alphheim", commandLore)

        //val servicesManager = Bukkit.getInternalServices() as CraftInternalServicesManager
        injector = AlphheimModule(this).createInjector()
        injector.injectMembers(this)

        registerCommands()
        registerListeners()
        enableComponents()

        //chatHandler = ChatHandlerService(this)
        //servicesManager.registerService(Chat::class.java, chatHandler, this,true)

    }

    private fun enableComponents() {
        permissionHandler = PermissionHandler(this)
        PluginCommandPermHandler(this)
        MotdHandler(this)
        FunHandler(this)
        SpawnHandler(this)
        tabHandler = TabHandler(this)
        tabListHandler = TabListHandler(this)
        healthHandler = HealthHandler(this)
        racialHandler = RacialHandler(this)
        RankCommands(this)
    }

    private fun registerCommands() {
        AlphheimCoreCommand(this)
    }

    private fun registerListeners() {
        PlayerListener(this)
    }

    private fun registerContexts() {
        //commandManager.commandCompletions.registerCompletion("groups",  {
            //it.input. permissionHandler.getGroups()
        //})

    }


    override fun onDisable() {

        //Bukkit.getInternalServices().unregisterService(Chat::class.java, chatHandler)
        Bukkit.getInternalServices().unregisterServices(this)

        if (commandLore != null) commandLore!!.unregister(Bukkit.getCommandMap());
        commandManager.unregisterCommands()

        permissionHandler.destruct() // Unregister components... Or, at least try to..

        MySQL.kill()
    }


}