/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.modules

import im.alphhe.alphheimplugin.EladriaCore
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider

class VaultModule(alphheimCore: EladriaCore) {

    var economy: Economy? = null
        private set
    var chat: Chat? = null
        private set
    var permissions: Permission? = null
        private set

    init {
        Bukkit.getPluginManager().getPlugin("Vault")
                ?: throw InstantiationException("Vault cannot be located!")

        val servicesManager = Bukkit.getServicesManager()

        val ecoRegistration: RegisteredServiceProvider<Economy>? = servicesManager.getRegistration(Economy::class.java)
        if (ecoRegistration == null) {
            alphheimCore.logger.warning("No ECO provider registered!")
        } else {
            economy = ecoRegistration.provider
            alphheimCore.logger.info("got eco provider! ${ecoRegistration.provider}")
        }

        val chatRegistration: RegisteredServiceProvider<Chat>? = servicesManager.getRegistration(Chat::class.java)
        if (chatRegistration == null) {
            alphheimCore.logger.warning("No Chat provider registered!")
        } else {
            chat = chatRegistration.provider
        }

        val permRegistration: RegisteredServiceProvider<Permission>? = servicesManager.getRegistration(Permission::class.java)
        if (permRegistration == null) {
            alphheimCore.logger.warning("No Chat provider registered!")
        } else {
            permissions = permRegistration.provider
        }

    }


}