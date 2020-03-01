/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.motd.listeners

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.server.ServerListPingEvent
import pw.valaria.aperture.components.motd.MotdHandler
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.ArrayList

class MotdListener(handler: MotdHandler) : Listener {
    private val lock = Any()

    private val pingMotdFile: File = File(handler.plugin.dataFolder, "slmotd.txt")
    private var pingMotd: String = ""
    private var pingLastMod = 0L

    private val joinMotdFile = File(handler.plugin.dataFolder, "motd.txt")
    private var joinLastModified = 0L
    private val joinMotd = ArrayList<String>()

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
        if (joinMotdFile.exists() && (joinMotdFile.lastModified() != joinLastModified)) {
            synchronized(lock) {
                if (joinMotdFile.exists() && (joinMotdFile.lastModified() != joinLastModified)) {
                    val nmotd = ArrayList<String>()
                    try {
                        Scanner(joinMotdFile).use { scanner ->

                            while (scanner.hasNextLine())
                                nmotd.add(ChatColor.translateAlternateColorCodes('&', scanner.nextLine()))

                            joinLastModified = joinMotdFile.lastModified()


                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    joinMotd.clear()
                    joinMotd.addAll(nmotd)
                }
            }
        }
    }


    @EventHandler
    fun serverPing(e: ServerListPingEvent) {
        updateSlMotd()
        if (pingMotd != "")
            e.motd = pingMotd
    }

    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
        sendMotd(e.player)
    }

    fun sendMotd(sender: CommandSender) {
        updateMotd()
        synchronized(lock) {
            if (!joinMotd.isEmpty()) {
                joinMotd.forEach { sender.sendMessage(it) }
            }
        }
    }


}
