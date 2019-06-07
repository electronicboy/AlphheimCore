/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.mmocredits

import com.gmail.nossr50.api.ExperienceAPI
import com.gmail.nossr50.api.SkillAPI
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.mmocredits.commands.CommandCredits
import pw.valaria.aperture.components.usermanagement.UserManager
import pw.valaria.aperture.utils.MySQL
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class MMOCreditsHandler(plugin: ApertureCore) : AbstractHandler(plugin) {
    private val validSkills = SkillAPI.getSkills()

    init {
        plugin.commandManager.commandCompletions.registerAsyncCompletion("mmoskills", {
            validSkills.map { it.toLowerCase() }
        })

        CommandCredits(plugin, this)


    }

    fun getCredits(player: OfflinePlayer): Int {
        MySQL.getConnection().use { conn ->
            val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(player.uniqueId)

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
        val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(player.uniqueId)
        MySQL.getConnection().use { conn ->
            conn.prepareStatement("INSERT INTO player_credits (PLAYER_ID, CREDITS) VALUES (?, ?) ON DUPLICATE KEY UPDATE CREDITS = CREDITS + VALUES(CREDITS)").use { stmt ->
                stmt.setInt(1, user.userID)
                stmt.setInt(2, amount)
                stmt.execute()
                return true // No exceptions occurred, this can't be bad.
            }
        }
    }

    fun takeCredits(player: OfflinePlayer, amount: Int): Boolean {
        val user = plugin.componentHandler.getComponent(UserManager::class.java)!!.getUser(player.uniqueId)
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

