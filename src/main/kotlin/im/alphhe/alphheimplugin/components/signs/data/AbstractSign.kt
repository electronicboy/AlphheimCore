/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.signs.data

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

abstract class AbstractSign(val plugin: Plugin, val location: Location) {

    // default to false, as some signs might not need additional data
    // Those that do can enforce it, as well as make those that don't need a lookup free
    fun hasData(): Boolean = false
    fun needsData(): Boolean = false

    abstract fun load(json: String)

    abstract fun update(config: String, value: Any): Boolean

    abstract fun save(): String

    abstract fun interact(player: Player)

    abstract fun render(player: Player): List<String>
}
