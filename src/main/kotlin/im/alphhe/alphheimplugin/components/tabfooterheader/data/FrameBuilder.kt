/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.tabfooterheader.data

class FrameBuilder() {

    private val frames = ArrayList<TabFrame>()
    private var frameTime = 0

    constructor(tabFrames: List<TabFrame>) : this() {
        frames.addAll(tabFrames)
    }

    fun add(header: String?, footer: String?, time: Int): FrameBuilder {
        frames.add(TabFrame(header, footer, time))
        frameTime += time
        return this
    }

    fun copyFrames(): ArrayList<TabFrame> {
        val list = ArrayList<TabFrame>(frames.size)
        list.addAll(frames)
        return list
    }


    fun build(): ArrayList<TabFrame> {
        val compiledFrames = ArrayList<TabFrame>(frameTime)
        for (frame in frames) {
            for (i in 0..frame.time) {
                compiledFrames.add(frame)
            }
        }
        return compiledFrames
    }


}