/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import im.alphhe.alphheimplugin.componenthandler.ComponentHandler
import im.alphhe.alphheimplugin.components.worldgen.WorldGenHandler
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
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

fun ComponentHandler.getComponent(kClass: KClass<WorldGenHandler>) {
    getComponent(kClass.java)
}