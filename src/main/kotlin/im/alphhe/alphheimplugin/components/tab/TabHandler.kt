/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.tab

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.tab.data.FrameBuilder
import im.alphhe.alphheimplugin.components.tab.data.TabFrame
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class TabHandler(plugin: AlphheimCore) : BukkitRunnable() {


    private var frames = ArrayList<TabFrame>()
    private var frame = HashMap<UUID, Int>()

    init {
        val header = "Alphheim // Mek pretti"
        val footer = "Mega pretti"

        frames = FrameBuilder()
                .add("1", "1")
                .add("2", "2")
                .add("3", "3")
                .add("4", "4")
                .build()


    }

    override fun run() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}