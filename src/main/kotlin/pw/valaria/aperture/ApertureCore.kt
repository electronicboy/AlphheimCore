/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture

import co.aikar.commands.PaperCommandManager
import me.lucko.luckperms.api.LuckPermsApi
import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap
import org.bukkit.generator.ChunkGenerator
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.plugin.java.JavaPlugin
import pw.valaria.aperture.commands.*
import pw.valaria.aperture.componenthandler.ComponentHandler
import pw.valaria.aperture.components.combat.CombatHandler
import pw.valaria.aperture.components.combattag.CombatTagHandler
import pw.valaria.aperture.components.diversions.FunHandler
import pw.valaria.aperture.components.donor.DonorManager
import pw.valaria.aperture.components.health.HealthHandler
import pw.valaria.aperture.components.mmocredits.MMOCreditsHandler
import pw.valaria.aperture.components.motd.MotdHandler
import pw.valaria.aperture.components.nicks.NickManager
import pw.valaria.aperture.components.permissions.PermissionHandler
import pw.valaria.aperture.components.plugincommandperms.PluginCommandPermHandler
import pw.valaria.aperture.components.racial.RacialHandler
import pw.valaria.aperture.components.rankcommands.RankCommands
import pw.valaria.aperture.components.restart.RestartHandler
import pw.valaria.aperture.components.spawn.SpawnHandler
import pw.valaria.aperture.components.tabfooterheader.TabHandler
import pw.valaria.aperture.components.tablist.TabListHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.components.voting.VoteHandler
import pw.valaria.aperture.components.worldgen.WorldGenHandler
import pw.valaria.aperture.listeners.PingListener
import pw.valaria.aperture.listeners.PlayerListener
import pw.valaria.aperture.listeners.SignListener
import pw.valaria.aperture.utils.MessageUtil
import pw.valaria.aperture.utils.MySQL


class ApertureCore : JavaPlugin() {

    val componentHandler = ComponentHandler(this)

    lateinit var commandManager: PaperCommandManager
    var commandLore: CommandLore? = null

    lateinit var luckPermsApi: LuckPermsApi

    private var consolePerms = mutableListOf<PermissionAttachment>()

    public val serverIntName = lazy { System.getProperty("serverName")}


    override fun onEnable() {

        try {
            registerConsolePerm("group.admin")
            registerConsolePerm("group.mod")
            registerConsolePerm("group.developer")

            MySQL.init(this)

            val provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)
            if (provider != null) {
                luckPermsApi = provider.provider
            } else {
                Bukkit.setWhitelist(true)
                logger.warning("Missing permission system?!!")
            }

            commandManager = PaperCommandManager(this)

            @Suppress("DEPRECATION")
            commandManager.enableUnstableAPI("help")

            val commandLore = CommandLore()

            val commandMap = (Bukkit.getCommandMap() as SimpleCommandMap)
            commandMap.getCommand("lore")?.unregister(Bukkit.getCommandMap())

            commandMap.register("lore", name.toLowerCase(), commandLore)

            registerCommands()
            registerListeners()
            enableComponents()

            for (player in Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("group.admin"))
                    MessageUtil.sendError(player, "successfully reloaded!")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            safeLockdown()
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
        componentHandler.registerComponent(WorldGenHandler::class.java)
        componentHandler.registerComponent(CombatTagHandler::class.java)
    }


    private fun registerCommands() {
        CommandCore(this)
        CommandSign(this)
        CommandEnderChest(this)
        CommandGC(this)
    }

    private fun registerListeners() {
        PlayerListener(this)
        SignListener(this)
        PingListener(this)
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

        try {
            Bukkit.getInternalServices().unregisterServices(this)
        } catch (ex: NoSuchMethodError) {
            // Don't log
        }


        if (commandLore != null) commandLore!!.unregister(Bukkit.getCommandMap())
        commandManager.unregisterCommands()

        componentHandler.disable()

        Bukkit.resetRecipes() // Cleanup...

        MySQL.kill()
        killConsolePerms()
    }

    fun restart(message: String = "We'll see you on the other side!") {
        server.onlinePlayers.forEach { it.kickPlayer(message) }
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator? {
        val worldGenHandler = componentHandler.getComponent(WorldGenHandler::class.java)!!
        return worldGenHandler.getGenerator(worldName, id) ?: worldGenHandler.emptyWorldGenerator
    }

    fun safeLockdown() {
        logger.info("Server is entering whitelist mode!")
        Bukkit.setWhitelist(true)
        Bukkit.getOnlinePlayers().forEach { player ->
            player.kickPlayer("An internal error has occurred and the server has now entered a lockdown state, please contact staff!")
        }
    }

    // This will probably come in handy...
    fun getServerName(): String {
        return "Esterwilde"
    }


}
