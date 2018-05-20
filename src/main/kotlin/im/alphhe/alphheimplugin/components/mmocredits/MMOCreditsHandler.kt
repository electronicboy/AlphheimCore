/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.mmocredits

import com.gmail.nossr50.api.ExperienceAPI
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.mmocredits.commands.CommandCredits
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class MMOCreditsHandler(private val plugin: AlphheimCore) {
    val validSkills = setOf("TAMING", "SWORDS", "ALCHEMY", "UNARMED", "ARCHERY", "AXES", "ACROBATICS", "FISHING", "EXCAVATION", "MINING", "HERBALISM", "REPAIR", "WOODCUTTING")

    init {
        plugin.commandManager.commandCompletions.registerAsyncCompletion("mmoskills", { _ ->
            validSkills.map { it.toLowerCase() }
        })

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

    fun takeCredits(player: OfflinePlayer, amount: Int): Boolean {
        val user = plugin.userManager.getUser(player.uniqueId)
        MySQL.getConnection().use { conn ->
            conn.autoCommit = false // Disable, we're about to do stuff that requires locking for the transaction....

            var credits = 0
            conn.prepareStatement("SELECT CREDITS FROM player_credits WHERE PLAYER_ID = ? FOR UPDATE").use { stmt ->
                stmt.setInt(1, user.userID)
                stmt.executeQuery().use { rs ->
                    // No results, means that we can't take from them...
                    if (!rs.next()) {
                        return false
                    }

                    credits = rs.getInt("CREDITS")
                }
            }

            val newAmount = credits - amount
            if (newAmount < 0) {
                return false
            }

            conn.prepareStatement("UPDATE player_credits SET CREDITS = CREDITS - ? WHERE PLAYER_ID = ?").use { stmt ->
                stmt.setInt(1, amount)
                stmt.setInt(2, user.userID)
                stmt.execute()
            }

            conn.commit()
            conn.autoCommit = true
            return true

        }


    }


    fun redeemCredits(player: Player, skill: String, amount: Int): Boolean {
        val skillUpper = skill.toUpperCase()
        if (!validSkills.contains(skillUpper)) {
            throw IllegalArgumentException("$skill is not a valid skill!")
        }

        val maxLimit = ExperienceAPI.getLevelCap(skillUpper)
        val currentLevel = ExperienceAPI.getLevel(player, skillUpper)
        val newLevel = currentLevel + amount

        if (newLevel > maxLimit) {
            throw IllegalArgumentException("Cannot exceed level cap!")
        }

        return if (!takeCredits(player, amount)) {
            false
        } else {
            ExperienceAPI.setLevel(player, skillUpper, newLevel)
            true
        }

    }


}

