/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.chat.formatter

import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.chat.ChatChannel

abstract class AbstractChatFormatter(val channel: ChatChannel, val plugin: ApertureCore) : IChatFormatter {

}