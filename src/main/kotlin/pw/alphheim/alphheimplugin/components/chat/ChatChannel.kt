/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin.components.chat

import net.md_5.bungee.api.ChatColor
import pw.alphheim.alphheimplugin.components.chat.formatter.IChatFormatter

import java.util.*

abstract class ChatChannel(val name: String, val chatColor: ChatColor, val shortName: String) {

    var chatFormatter: LinkedList<IChatFormatter> = LinkedList()


}