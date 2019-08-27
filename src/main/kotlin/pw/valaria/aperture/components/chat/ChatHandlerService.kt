/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat

import net.kyori.text.TextComponent
import net.kyori.text.adapter.bukkit.TextAdapter
import net.kyori.text.format.TextColor
import net.kyori.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.data.AlphheimUser
import org.bukkit.entity.Player
import org.bukkit.plugin.ServicePriority
import pw.valaria.aperture.translateColors
import pw.valaria.api.services.Chat


class ChatHandlerService(apertureCore: ApertureCore) : AbstractHandler(apertureCore),  pw.valaria.api.services.Chat {

    var useFallback = true;
    var userManager: UserManager = apertureCore.componentHandler.getComponentOrThrow(UserManager::class.java)
    val channels: Map<String, ChatChannel> = mutableMapOf()
    val users: Set<AlphheimUser> = linkedSetOf()
    //val tempChannel = GlobalChatChannel("OOC", ChatColor.RED, "OOC")
    //val tempChannel = ChatChannel()

    init {
        Bukkit.getInternalServices().registerService(Chat::class.java, this, apertureCore)
    }

    fun addUser(player: Player) {
        val user = userManager.getUser(player)
        channels.forEach { _, chan ->
            if (chan.canJoin(user)) {
                chan.join(user)
            }
        }

    }

    override fun process(sender: Player, message: String) {
        if (useFallback) {
            doFallbackChat(sender, message)
            return;
        }

    }

    private fun doFallbackChat(sender: Player, message: String) {
        val displayName = sender.displayName ?: sender.name
        val displayNameComp = LegacyComponentSerializer.INSTANCE.deserialize(displayName.translateColors())

        var messageComponent = if (sender.hasPermission("aperture.color")) {
            LegacyComponentSerializer.INSTANCE.deserialize(message.translateColors())
        } else {
            TextComponent.of(message)
        }

        TextComponent.builder()
                .content("> ")
                .color(TextColor.DARK_RED)
                .append(displayNameComp)
                .append(":")
                .color(TextColor.DARK_GRAY)
                .append(" ").let {
                    if (sender.hasPermission("aperture.color")) {

                    }


                }
    }

    override fun onDisable() {
        Bukkit.getInternalServices().unregisterService(Chat::class.java, this)
    }
}
