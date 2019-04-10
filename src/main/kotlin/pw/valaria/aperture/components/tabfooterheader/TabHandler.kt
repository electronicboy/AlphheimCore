/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.tabfooterheader

import com.google.gson.GsonBuilder
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.tabfooterheader.data.FrameBuilder
import pw.valaria.aperture.components.tabfooterheader.data.TabFrame
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader
import java.util.*

class TabHandler(plugin: ApertureCore) : AbstractHandler(plugin) {


    private var frames = ArrayList<TabFrame>()
    private var frame = HashMap<UUID, Int>()
    private var tabFile = File(plugin.dataFolder, "tabframes.txt")
    private val lock = Any()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    init {

        if (!tabFile.exists()) {
            val saveFrames = FrameBuilder()
                    .add("1", "1", 20)
                    .add("2", "2", 20)
                    .add("3", "3", 20)
                    .add("4", "4", 20)
                    .copyFrames()
            val fileWriter = FileWriter(tabFile)

            gson.toJson(saveFrames, fileWriter)
            fileWriter.close()
        }

        reset()

        // TODO: Extract to seperate class
        val runnable = object : BukkitRunnable() {
            override fun run() {
                synchronized(lock) {
                    for (player in plugin.server.onlinePlayers) {
                        var playerFrame = frame[player.uniqueId] ?: 0

                        sendFrame(player, frames[playerFrame])
                        ++playerFrame
                        if (playerFrame == frames.size) {
                            frame[player.uniqueId] = 0
                        } else {
                            frame[player.uniqueId] = playerFrame
                        }

                    }
                }
            }
        }

        runnable.runTaskTimerAsynchronously(plugin, 0, 10)
    }


    fun sendFrame(player: Player, frame: TabFrame) {
        val header = processReplacements(frame.header, player)
        val footer = processReplacements(frame.footer, player)
        player.setPlayerListHeaderFooter(header, footer)

    }

    fun reset() {
        synchronized(lock) {
            frame.clear() // because people aren't going to always be looking at the first...
            try {
                val fileReader = FileInputStream(tabFile)
                val inputStreamReader = InputStreamReader(fileReader, Charsets.UTF_8)
                val loadFrames: List<TabFrame> = gson.fromJson(inputStreamReader, Array<TabFrame>::class.java).toList()

                frames = FrameBuilder(loadFrames).build()

            } catch (e: Throwable) {
                e.printStackTrace()
                // Shane fucked up....
                frames = FrameBuilder()
                        .add("${ChatColor.DARK_RED}${ChatColor.STRIKETHROUGH}--------<-({------}}->--------",
                                "${ChatColor.DARK_RED}${ChatColor.STRIKETHROUGH}--------<-({------}}->--------", 1)
                        .add("${ChatColor.DARK_GRAY}${ChatColor.STRIKETHROUGH}--------<-({------}}->--------",
                                "${ChatColor.DARK_GRAY}${ChatColor.STRIKETHROUGH}--------<-({------}}->--------", 2)
                        .build()
            }
        }
    }

    private fun processReplacements(input: String?, target: Player): String {
        if (input == null) return ""

        val output = input
                .replace("{DISPLAYNAME}", target.displayName)
        return ChatColor.translateAlternateColorCodes('&', output)
    }

}