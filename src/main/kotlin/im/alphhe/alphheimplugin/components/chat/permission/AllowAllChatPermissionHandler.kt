/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.chat.permission

import org.bukkit.entity.Player

class AllowAllChatPermissionHandler : ChatPermissionHandler {
    override fun canJoin(player: Player): Boolean {
        return true
    }

}