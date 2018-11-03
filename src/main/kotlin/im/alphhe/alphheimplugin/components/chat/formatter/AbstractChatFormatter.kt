/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.formatter

import im.alphhe.alphheimplugin.EladriaCore
import im.alphhe.alphheimplugin.components.chat.ChatChannel

abstract class AbstractChatFormatter(@Suppress("UNUSED_PARAMETER") channel: ChatChannel, plugin: EladriaCore) : IChatFormatter {
    init {
        plugin.injector.injectMembers(this)
    }

}