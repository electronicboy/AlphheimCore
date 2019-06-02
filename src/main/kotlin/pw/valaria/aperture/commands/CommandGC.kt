/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import net.kyori.text.TextComponent
import net.kyori.text.event.HoverEvent
import net.kyori.text.format.TextColor
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.checkerframework.checker.nullness.qual.NonNull
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.utils.MessageUtil
import java.lang.management.ManagementFactory
import java.time.Duration

class CommandGC(val core: ApertureCore) : CoreCommand(core) {


    @CommandPermission("alphheim.admin")
    @CommandAlias("gc|mem")
    fun execute(sender: CommandSender) {
        val uptime = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().startTime - System.currentTimeMillis())


        val messages = ArrayList<TextComponent>()

        messages.add(keyToValString("Uptime", MessageUtil.durationToString(uptime)))
        messages.add(getTps())
        messages.add(formatMemory())
        core.server.worlds.forEach { world -> messages.add(formatWorld(world)) }

        messages.forEach { MessageUtil.sendInfo(sender, it)}


    }

    private fun getTps(): TextComponent {
        val tps = core.server.tps
        return TextComponent.builder("TPS: ").color(TextColor.GRAY)
                .append(tps[0].toString()).color(getTPSTextColor(tps[0]))
                .append(", ").color(TextColor.DARK_GRAY)
                .append(tps[1].toString()).color(getTPSTextColor(tps[1]))
                .append(", ").color(TextColor.DARK_GRAY)
                .append(tps[2].toString()).color(getTPSTextColor(tps[2]))
                .build()
    }


    private fun keyToValString(key: String, value: String): TextComponent {
        return TextComponent.builder("$key: ").color(TextColor.GRAY).append(value).color(TextColor.DARK_GREEN).build()
    }

    private fun getTPSTextColor(tps: Double): TextColor {
        return if (tps > 18.0) {
            TextColor.GREEN
        } else {
            if (tps > 16.0) {
                TextColor.YELLOW
            } else {
                TextColor.RED
            }
        }
    }

    private fun formatMemory(): @NonNull TextComponent {
        return TextComponent.builder("Mem: ", TextColor.GRAY)
                .append("F:", TextColor.DARK_GRAY).append(" " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB", TextColor.DARK_GREEN)
                .append(", ", TextColor.GRAY)
                .append("A:", TextColor.DARK_GRAY).append(" " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB", TextColor.DARK_GREEN)
                .append(", ", TextColor.GRAY)
                .append("M:", TextColor.DARK_GRAY).append(" " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB", TextColor.DARK_GREEN)
                .build()

    }

    private fun formatWorld(world: World): TextComponent {
        return TextComponent.builder(world.name, TextColor.GRAY)
                .hoverEvent(HoverEvent.showText(
                        TextComponent.builder("UUID: ", TextColor.GRAY).append(world.uid.toString(), TextColor.DARK_GREEN).build()
                ))
                .append(" ")
                .append("C:", TextColor.DARK_GRAY).append(" " + world.loadedChunks.size)
                .append(", ", TextColor.GRAY)
                .append("P:", TextColor.DARK_GRAY).append(" " + world.playerCount)
                .append(", ", TextColor.GRAY)
                .append("E:", TextColor.DARK_GRAY).append(" " + world.entityCount)
                .append(", ", TextColor.GRAY)
                .append("TE:", TextColor.DARK_GRAY).append(" " + world.tileEntityCount)

                .build()
    }
}


