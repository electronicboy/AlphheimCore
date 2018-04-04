/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.tab.data

class FrameBuilder() {

    private val frames = ArrayList<TabFrame>()


    fun add(header: String?, footer: String?): FrameBuilder {
        frames.add(TabFrame(header, footer))

        return this
    }

    fun build(): ArrayList<TabFrame> {
        return frames
    }


}