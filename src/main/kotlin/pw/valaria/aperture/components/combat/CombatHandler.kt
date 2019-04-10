/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.combat

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.combat.listeners.CombatListener
import pw.valaria.aperture.components.combat.listeners.PotionListener
import org.bukkit.Bukkit
import org.bukkit.Material
import java.util.*

class CombatHandler(plugin: ApertureCore) : AbstractHandler(plugin) {
    private val toRemove: EnumSet<Material> = EnumSet.noneOf(Material::class.java)


    init {
        CombatListener(plugin)
        PotionListener(plugin)

        toRemove.add(Material.ELYTRA)

        val recipeIterator = Bukkit.getServer().recipeIterator()
        while (recipeIterator.hasNext()) {
            val recipe = recipeIterator.next()
            if (toRemove.contains(recipe.result.type)) {
                recipeIterator.remove()
            }
        }
    }


}