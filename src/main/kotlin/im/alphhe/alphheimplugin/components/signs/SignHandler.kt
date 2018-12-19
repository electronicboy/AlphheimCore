/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.signs

import com.google.common.collect.HashBasedTable
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.signs.data.AbstractSign
import org.bukkit.Chunk
import org.bukkit.Location
import kotlin.reflect.KClass

class SignHandler(plugin: EladriaCore) : AbstractHandler(plugin) {
    val signProviders = HashMap<String, KClass<AbstractSign>>()
    val loadedSigns =  HashMap<Location, AbstractSign>();

    init {


    }

    fun chunkLoadEvent() {}
}