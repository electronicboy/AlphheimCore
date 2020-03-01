/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.utils

import net.kyori.text.TextComponent
import net.kyori.text.adapter.bukkit.TextAdapter
import net.kyori.text.format.TextColor
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.time.Duration


object MessageUtil {
    private val default = ChatColor.GREEN
    private val error = ChatColor.DARK_RED
    private val prefix = "${ChatColor.DARK_GRAY}[${ChatColor.RED}V${ChatColor.DARK_GRAY}]"

    private fun send(player: CommandSender, s: String) {
        player.sendMessage("$prefix $s")
    }

    fun sendInfo(player: CommandSender, s: String) {
        send(player, "$default$s")
    }

    fun sendError(player: CommandSender, s: String) {
        send(player, "$error$s")
    }

    fun broadcast(perm: String?, message: String) {
        Bukkit.getConsoleSender().sendMessage(message)
        for (player in Bukkit.getOnlinePlayers()) {
            if (perm == null || player.hasPermission(perm))
                sendInfo(player, message)
        }
    }

    fun broadcast(message: String) {
        broadcast(null, message)
    }

    fun format(prefix: String = "A", color: ChatColor = ChatColor.RED): String {
        return "${ChatColor.DARK_GRAY}[$color$prefix${ChatColor.DARK_GRAY}] "
    }

    fun sendInfo(player:CommandSender, component: TextComponent) = MessageUtil.sendInfo(player, TextColor.RED, component)

    fun sendInfo(player: CommandSender, defaultColor: TextColor, component: TextComponent) {
        val toSend = TextComponent.builder()
                .content("[")
                .color(TextColor.DARK_GRAY)
                .append(TextComponent.of("V").color(TextColor.RED))
                .append(TextComponent.of("]").color(TextColor.DARK_GRAY))
                .append(TextComponent.of(" ").color(defaultColor))
                .append(component)
                .build()
        TextAdapter.sendComponent(player, toSend)
    }

    fun durationToString(duration: Duration): String {
        val seconds = (duration.toMillis() / 1000) % 60
        val minutes = duration.toMinutes() % 60
        val hours = duration.toHours() % 24
        val days = duration.toHours() / 24

        val sb = StringBuilder("")
        var hasAppended = false
        if (days != 0L) {
            sb.append("$days day")
            if (days > 1) {
                sb.append("s")
            }
            hasAppended = true
        }
        if (hours != 0L) {
            if (hasAppended) sb.append(", ")
            sb.append("$hours hour")
            if (hours > 1) {
                sb.append("s")
            }
            hasAppended = true
        }
        if (minutes != 0L || hasAppended) {
            if (hasAppended) sb.append(", ")
            sb.append("$minutes minute")
            if (minutes > 1) {
                sb.append("s")
            }
            hasAppended = true
        }
        if (seconds != 0L || hasAppended || (!hasAppended && seconds == 0L)) {
            if (hasAppended) sb.append(", ")
            val secondsToDis = if (seconds == 0L) {
                1L
            } else seconds

            sb.append("$secondsToDis seconds")
        }
        return sb.toString()
    }

}