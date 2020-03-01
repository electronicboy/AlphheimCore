/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.diversions

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.diversions.commands.CommandRoulette
import pw.valaria.aperture.components.diversions.listeners.EdibleListener
import pw.valaria.aperture.components.diversions.listeners.FurnaceListener

class FunHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    init {
        plugin.server.pluginManager.registerEvents(FurnaceListener(), plugin)
        plugin.server.pluginManager.registerEvents(EdibleListener(plugin), plugin)
        plugin.commandManager.registerCommand(CommandRoulette(plugin))

        try { registerRecipes() } catch (ex: Exception) {}

    }

    private fun registerRecipes() {
        val saddleRecipe = ShapedRecipe(NamespacedKey(plugin, "saddle"), ItemStack(Material.SADDLE))
        saddleRecipe.shape("sll", "lxx", "lll")
        saddleRecipe.setIngredient('s', Material.STRING)
        saddleRecipe.setIngredient('l', Material.LEATHER)
        saddleRecipe.setIngredient('x', Material.STICK)
        plugin.server.addRecipe(saddleRecipe)
    }

}