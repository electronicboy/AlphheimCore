/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.tablist

import com.google.gson.GsonBuilder
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.tablist.data.FrameBuilder
import im.alphhe.alphheimplugin.components.tablist.data.TabFrame
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class TabHandler(private val plugin: AlphheimCore) : BukkitRunnable() {


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

        runTaskTimerAsynchronously(plugin, 0, 10)
    }

    @Synchronized
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

    fun sendFrame(player: Player, frame: TabFrame) {
        val header = processReplacements(frame.header, player)
        val footer = processReplacements(frame.footer, player)




        val headerComponent = TextComponent.fromLegacyText(header)
        val footerComponent = TextComponent.fromLegacyText(footer)

        player.setPlayerListHeaderFooter(headerComponent, footerComponent)

    }

    fun reset() {
        synchronized(lock) {
            try {
                val fileReader = FileReader(tabFile)
                val loadFrames: List<TabFrame> = gson.fromJson(fileReader, Array<TabFrame>::class.java).toList()

                frames = FrameBuilder(loadFrames).build()
                frame.clear() // because people aren't going to always be looking at the first...

            } catch (e: Throwable) {
            } // Don't do anything in case it fucks up....
        }
    }

    private fun processReplacements(input: String?, target: Player): String {
        if (input == null) return ""

        val output = input
                .replace("{DISPLAYNAME}", target.displayName)
        return ChatColor.translateAlternateColorCodes('&', output)
    }


}