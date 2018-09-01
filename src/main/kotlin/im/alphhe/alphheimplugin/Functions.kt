/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import java.util.ArrayList
import java.util.regex.Pattern

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

fun ChatColor.color(text: String) : String  = ChatColor.translateAlternateColorCodes('&', text)

fun String.toComponents(): Array<BaseComponent> {
    return toComponents(ChatColor.WHITE)
}

fun String.toComponents(defaultColor: ChatColor): Array<BaseComponent> {
    val components = ArrayList<BaseComponent>()
    var builder = StringBuilder()
    var component = TextComponent()
    val matcher = url.matcher(this)

    var i = 0
    while (i < this.length) {
        var c = this[i]
        var old: TextComponent
        if (c.toInt() == 167) {
            ++i
            if (i >= this.length) {
                break
            }

            c = this[i]
            if (c in 'A'..'Z') {
                c = (c.toInt() + 32).toChar()
            }

            var format: net.md_5.bungee.api.ChatColor? = net.md_5.bungee.api.ChatColor.getByChar(c)
            if (format != null) {
                if (builder.isNotEmpty()) {
                    component.text = builder.toString()
                    components.add(component)
                    component = TextComponent()
                }

                when (format) {
                    net.md_5.bungee.api.ChatColor.BOLD -> component.isBold = true
                    net.md_5.bungee.api.ChatColor.ITALIC -> component.isItalic = true
                    net.md_5.bungee.api.ChatColor.UNDERLINE -> component.isUnderlined = true
                    net.md_5.bungee.api.ChatColor.STRIKETHROUGH -> component.isStrikethrough = true
                    net.md_5.bungee.api.ChatColor.MAGIC -> component.isObfuscated = true
                    net.md_5.bungee.api.ChatColor.RESET -> {
                        format = defaultColor
                        component = TextComponent()
                        component.color = format
                    }
                    else -> {
                        component = TextComponent()
                        component.color = format
                    }
                }
            }
        } else {
            var pos = this.indexOf(32.toChar(), i)
            if (pos == -1) {
                pos = this.length
            }

            if (matcher.region(i, pos).find()) {
                if (builder.isNotEmpty()) {
                    component.text = builder.toString()
                    components.add(component)
                }

                component = TextComponent()
                val urlString = this.substring(i, pos)
                component.text = urlString
                component.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, if (urlString.startsWith("http")) urlString else "http://$urlString")
                components.add(component)
                i += pos - i - 1
            } else {
                builder.append(c)
            }
        }
        ++i
    }

    component.text = builder.toString()
    components.add(component)
    return components.toTypedArray()
}