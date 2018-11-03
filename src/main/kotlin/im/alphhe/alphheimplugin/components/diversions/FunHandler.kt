/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.diversions

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import im.alphhe.alphheimplugin.components.diversions.commands.CommandRoulette
import im.alphhe.alphheimplugin.components.diversions.listeners.EdibleListener
import im.alphhe.alphheimplugin.components.diversions.listeners.FurnaceListener
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class FunHandler(plugin: EladriaCore) : AbstractHandler(plugin) {

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