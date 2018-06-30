/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import co.aikar.commands.BukkitCommandManager
import com.google.inject.Injector
import im.alphhe.alphheimplugin.commands.CommandCore
import im.alphhe.alphheimplugin.commands.CommandEnderChest
import im.alphhe.alphheimplugin.commands.CommandLore
import im.alphhe.alphheimplugin.commands.CommandSign
import im.alphhe.alphheimplugin.componenthandler.ComponentHandler
import im.alphhe.alphheimplugin.components.usermanagement.UserManager
import im.alphhe.alphheimplugin.components.chat.ChatHandlerService
import im.alphhe.alphheimplugin.components.combat.CombatHandler
import im.alphhe.alphheimplugin.components.diversions.FunHandler
import im.alphhe.alphheimplugin.components.donor.DonorManager
import im.alphhe.alphheimplugin.components.health.HealthHandler
import im.alphhe.alphheimplugin.components.mmocredits.MMOCreditsHandler
import im.alphhe.alphheimplugin.components.motd.MotdHandler
import im.alphhe.alphheimplugin.components.nicks.NickManager
import im.alphhe.alphheimplugin.components.permissions.PermissionHandler
import im.alphhe.alphheimplugin.components.plugincommandperms.PluginCommandPermHandler
import im.alphhe.alphheimplugin.components.racial.RacialHandler
import im.alphhe.alphheimplugin.components.rankcommands.RankCommands
import im.alphhe.alphheimplugin.components.restart.RestartHandler
import im.alphhe.alphheimplugin.components.spawn.SpawnHandler
import im.alphhe.alphheimplugin.components.tabfooterheader.TabHandler
import im.alphhe.alphheimplugin.components.tablist.TabListHandler
import im.alphhe.alphheimplugin.components.voting.VoteHandler
import im.alphhe.alphheimplugin.listeners.PlayerListener
import im.alphhe.alphheimplugin.listeners.SignListener
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.MySQL
import me.lucko.luckperms.api.LuckPermsApi
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.plugin.java.JavaPlugin


class AlphheimCore : JavaPlugin() {

    public val componentHandler = ComponentHandler(this)

    @Deprecated("Use ComponentHandler!!!")
    lateinit var injector: Injector
        private set
    lateinit var commandManager: BukkitCommandManager
    var commandLore: CommandLore? = null

    lateinit var luckPermsApi: LuckPermsApi

    private var consolePerms = mutableListOf<PermissionAttachment>()


    override fun onEnable() {

        registerConsolePerm("alphheim.admin")
        registerConsolePerm("alphheim.mod")
        registerConsolePerm("alphheim.dev")

        MySQL.init(this)

        val provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)
        if (provider != null) {
            luckPermsApi = provider.provider
        }

        //userManager = UserManager(this)
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

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("alphheim.admin"))
                MessageUtil.sendError(player, "AlphheimCore has successfully reloaded!")
        }

        //chatHandler = ChatHandlerService(this)
        //servicesManager.registerService(Chat::class.java, chatHandler, this,true)
    }

    private fun enableComponents() {
        componentHandler.registerComponent(UserManager::class.java)
        componentHandler.registerComponent(PermissionHandler::class.java)
        componentHandler.registerComponent(PluginCommandPermHandler::class.java)
        componentHandler.registerComponent(MotdHandler::class.java)
        componentHandler.registerComponent(FunHandler::class.java)
        componentHandler.registerComponent(SpawnHandler::class.java)
        componentHandler.registerComponent(TabHandler::class.java)
        componentHandler.registerComponent(TabListHandler::class.java)
        componentHandler.registerComponent(HealthHandler::class.java)
        componentHandler.registerComponent(RacialHandler::class.java)
        componentHandler.registerComponent(RankCommands::class.java)
        componentHandler.registerComponent(NickManager::class.java)
        componentHandler.registerComponent(DonorManager::class.java)
        componentHandler.registerComponent(CombatHandler::class.java)
        componentHandler.registerComponent(VoteHandler::class.java)
        componentHandler.registerComponent(MMOCreditsHandler::class.java)
        componentHandler.registerComponent(RestartHandler::class.java)
    }

    private fun registerCommands() {
        CommandCore(this)
        CommandSign(this)
        CommandEnderChest(this)
    }

    private fun registerListeners() {
        PlayerListener(this)
        SignListener(this)
    }


    private fun registerConsolePerm(perm: String) {
        consolePerms.add(Bukkit.getConsoleSender().addAttachment(this, perm, true))
    }

    private fun killConsolePerms() {
        for (attach in consolePerms) {
            attach.remove()
        }
    }


    override fun onDisable() {

        //Bukkit.getInternalServices().unregisterService(Chat::class.java, chatHandler)
        Bukkit.getInternalServices().unregisterServices(this)

        if (commandLore != null) commandLore!!.unregister(Bukkit.getCommandMap());
        commandManager.unregisterCommands()

        componentHandler.getComponent(VoteHandler::class.java)?.destruct()
        componentHandler.getComponent(PermissionHandler::class.java)!!.destruct()

        componentHandler.disable()

        MySQL.kill()
        killConsolePerms();
    }

    fun restart(message: String = "We'll see you on the other side!") {
        server.onlinePlayers.forEach { it.kickPlayer(message) }
    }


}