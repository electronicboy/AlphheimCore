/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent

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

fun ChatColor.color(text: String) : String  = ChatColor.translateAlternateColorCodes('&', text)