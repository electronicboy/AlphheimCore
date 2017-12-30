/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin.components

import pw.alphheim.alphheimplugin.AlphheimCore

interface Component {

    fun enable(core: AlphheimCore)

    fun disable()

    fun postEnable()
}