/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.combat

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.combat.listeners.CombatListener
import im.alphhe.alphheimplugin.components.combat.listeners.PotionListener

class CombatHandler(plugin: AlphheimCore) : AbstractHandler(plugin) {


    init {
        CombatListener(plugin)
        PotionListener(plugin)
    }


}