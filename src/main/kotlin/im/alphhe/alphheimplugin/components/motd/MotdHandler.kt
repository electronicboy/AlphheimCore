/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.motd

import im.alphhe.alphheimplugin.AlphheimCore
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.server.ServerListPingEvent
import java.io.*
import java.util.*

class MotdHandler(val plugin: AlphheimCore) : Listener {
    private val lock = Any()

    private val pingMotdFile: File
    private var pingMotd: String? = null
    private var pingLastMod = 0L

    private var joinLastModified = 0L
    private var joinMotd: List<String>? = null


    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
        pingMotdFile = File(plugin.dataFolder, "slmotd.txt")

        updateSlMotd()
    }

    private fun updateSlMotd() {

        if (pingMotdFile.exists() && (pingMotdFile.lastModified() != pingLastMod)) {
            synchronized(lock) {
                if (pingMotdFile.exists() && (pingMotdFile.lastModified() != pingLastMod)) {
                    val nmotd = StringBuilder()
                    try {
                        Scanner(pingMotdFile).use { scanner ->

                            while (scanner.hasNextLine()) {
                                nmotd.append(ChatColor.translateAlternateColorCodes('&', scanner.nextLine()))
                                if (scanner.hasNext())
                                    nmotd.append("\n")
                            }

                            pingLastMod = pingMotdFile.lastModified()


                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    pingMotd = nmotd.toString()
                }
            }
        }
    }

    private fun updateMotd() {
        val file = File(plugin.dataFolder, "motd.txt")

        if (file.exists() && (file.lastModified() != joinLastModified)) {
            synchronized(lock) {
                if (file.exists() && (file.lastModified() != joinLastModified)) {
                    val nmotd = ArrayList<String>()
                    try {
                        Scanner(file).use { scanner ->

                            while (scanner.hasNextLine())
                                nmotd.add(ChatColor.translateAlternateColorCodes('&', scanner.nextLine()))

                            joinLastModified = file.lastModified()


                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    joinMotd = nmotd
                }
            }
        }
    }


    @EventHandler
    fun serverPing(e: ServerListPingEvent) {
        updateSlMotd()
        if (pingMotd != null)
            e.motd = pingMotd
    }

    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
        updateMotd()
        synchronized(lock) {
            if (joinMotd != null) {
                joinMotd!!.forEach { e.player.sendMessage(it) }
            }
        }

    }


}
