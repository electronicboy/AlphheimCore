/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.combat

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.combat.listeners.CombatListener
import im.alphhe.alphheimplugin.components.combat.listeners.PotionListener
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*

class CombatHandler(plugin: EladriaCore) : AbstractHandler(plugin) {
    val toRemove: EnumSet<Material> = EnumSet.noneOf(Material::class.java)


    init {
        CombatListener(plugin)
        PotionListener(plugin)

        toRemove.add(Material.ELYTRA)

        val recipeIterator = Bukkit.getServer().recipeIterator();
        while (recipeIterator.hasNext()) {
            val recipe = recipeIterator.next()
            if (toRemove.contains(recipe.result.type)) {
                recipeIterator.remove()
            }
        }
    }


}