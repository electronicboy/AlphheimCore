/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture

import pw.valaria.aperture.componenthandler.ComponentHandler
import pw.valaria.aperture.components.worldgen.WorldGenHandler
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.checkerframework.checker.nullness.qual.NonNull
import pw.valaria.aperture.utils.MessageUtil
import java.time.Duration
import java.util.regex.Pattern
import kotlin.reflect.KClass

private val url = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$")

fun ComponentBuilder.addComponent(component: BaseComponent) {
    val textComponent = component as? TextComponent ?: return

    this.append(textComponent.text)
    this.apply {
        color(textComponent.color)

        strikethrough(textComponent.isStrikethrough)

        bold(textComponent.isBold)
        italic(textComponent.isItalic)
        obfuscated(textComponent.isObfuscated)

        event(textComponent.hoverEvent)
        event(textComponent.clickEvent)
    }

}

//public fun ComponentBuilder.append(components: Array<out BaseComponent>) {
//    components.forEach { component -> addComponent(component) }
//}

fun ChatColor.color(text: String): String = ChatColor.translateAlternateColorCodes('&', text)

fun String.toComponents(): Array<BaseComponent> {
    return TextComponent.fromLegacyText(this)
}

fun Duration.toRemainingString(): String {
    val sb = StringBuilder()

    if (this.toHours() % 24 != 0L) {
        sb.append("${this.toHours() % 24}h")
    }

    if (sb.isNotEmpty() || this.toMinutes() % 60 != 0L) {
        if (sb.isNotEmpty()) sb.append(", ")
        sb.append("${this.toMinutes() % 60}m")
    }
    if (sb.isNotEmpty()) sb.append(", ")
    sb.append("${this.seconds % 60}s")

    if (this.seconds == 0L) {
        sb.append(this.toMillis()).append("ms")
    }

    return sb.toString()
}

fun String.translateColors(): @NonNull String {
    return ChatColor.translateAlternateColorCodes('&', this)
}