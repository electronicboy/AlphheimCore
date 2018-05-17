/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.mmocedits

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.mmocedits.commands.CommandCredits
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.OfflinePlayer

class MMOCreditsHandler(private val plugin: AlphheimCore) {
    init {
        CommandCredits(plugin, this)
    }

    fun getCredits(player: OfflinePlayer): Int {
        MySQL.getConnection().use { conn ->
            val user = plugin.userManager.getUser(player.uniqueId)

            conn.prepareStatement("SELECT CREDITS FROM player_credits WHERE PLAYER_ID = ?").use {
                it.setInt(1, user.userID)
                it.executeQuery().use { rs ->
                    return if (!rs.next()) {
                        0
                    } else {
                        rs.getInt("CREDITS")

                    }
                }
            }


        }
    }

    fun giveCredits(player: OfflinePlayer, amount: Int): Boolean {
        val user = plugin.userManager.getUser(player.uniqueId)

        MySQL.getConnection().use { conn ->
            conn.prepareStatement("INSERT INTO player_credits (PLAYER_ID, CREDITS) VALUES (?, ?) ON DUPLICATE KEY UPDATE CREDITS = CREDITS + VALUES(CREDITS)").use { stmt ->
                stmt.setInt(1, user.userID)
                stmt.setInt(2, amount)
                return stmt.execute()
            }
        }
    }




}

