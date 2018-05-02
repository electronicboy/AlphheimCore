/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.combat

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.combat.listeners.CombatListener

class CombatHandler(private val plugin: AlphheimCore) {


    init {
        CombatListener(plugin)
    }


}