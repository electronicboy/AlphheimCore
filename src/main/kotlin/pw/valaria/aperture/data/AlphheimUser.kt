/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.data

import pw.valaria.aperture.components.chat.ChatStatus
import pw.valaria.aperture.utils.MySQL
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.sql.Statement
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * Created by shane on 8/25/16.
 */
class AlphheimUser(val uuid: UUID, @Suppress("UNUSED_PARAMETER") isNPC: Boolean = false) {

    private var lastUpdated = 0L

    var userID: Int = -1
        private set
    private var nickname: String? = null

    var activeChannel: String? = null
    var channelData: Map<String, ChatStatus> = mutableMapOf()

    private val cooldowns = HashMap<String, Long>()

    var overrides = false
    var firstJoin = false

    private fun getPlayer(): Player? {
        return Bukkit.getPlayer(uuid)
    }

    init {
        MySQL.getConnection().use { conn ->
            val prepareStatement = conn.prepareStatement("SELECT PLAYER_ID FROM player_data WHERE PLAYER_UUID = ?")
            prepareStatement.use { statement ->
                statement.setString(1, uuid.toString())
                if (statement.execute()) {
                    val resultSet = statement.resultSet

                    resultSet.use { rs ->
                        if (rs.next()) {
                            userID = rs.getInt(rs.findColumn("PLAYER_ID"))
                        }
                    }
                }
            }
        }

        if (userID == -1) {
            firstJoin = true
            MySQL.getConnection().use { connection ->
                val statement1 = connection.prepareStatement("INSERT INTO player_data (PLAYER_UUID) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
                statement1.use { insertStatement ->

                    insertStatement.setString(1, uuid.toString())
                    insertStatement.executeUpdate()

                    insertStatement.use { ps ->
                        ps.generatedKeys.use { rs ->
                            if (rs.next())
                                userID = rs.getInt(1)
                        }
                    }
                }
            }
        }

        updateData()
    }

    @Synchronized()
    fun updateData() {
        if (System.currentTimeMillis() <= lastUpdated + TimeUnit.SECONDS.toMillis(10)) return

        MySQL.getConnection().use { conn ->
            conn.prepareStatement("SELECT NAME, EXPIRY FROM cooldowns WHERE PLAYER_ID = ?").use { stmt ->
                stmt.setInt(1, userID)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        try {
                            val string = rs.getString("NAME")
                            val timestamp = rs.getLong("EXPIRY")
                            cooldowns[string] = timestamp
                        } catch (ignored: Exception) {
                        } // this should never happen, but I really don't wanna deal with the potential that it does...
                    }
                }
            }

            conn.prepareStatement("SELECT NICKNAME FROM player_nicks WHERE PLAYER_ID = ?").use { stmt ->
                stmt.setInt(1, userID)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        nickname = rs.getString("NICKNAME")
                        if (nickname != null) {
                            getPlayer()?.setDisplayName(ChatColor.translateAlternateColorCodes('&', nickname!!.replace(' ', '_')))
                        }
                    }
                }

            }
        }

    }


    fun getCooldown(name: String): Long? {
        return cooldowns[name]
    }

    fun setCooldown(name: String, expiry: Long) {
        cooldowns[name] = expiry

        MySQL.executor.execute {
            MySQL.getConnection().use { conn ->
                val statement = conn.prepareStatement("INSERT INTO cooldowns (PLAYER_ID, NAME, EXPIRY) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE EXPIRY = VALUES(EXPIRY)")
                statement.use { stmt ->
                    stmt.setInt(1, userID)
                    stmt.setString(2, name)
                    stmt.setLong(3, expiry)
                    stmt.execute()
                }
            }
        }
    }

    fun hasOverrides(): Boolean = overrides


    fun getNickname(): String {
        val nick = if (nickname != null) {
            nickname!!
        } else {
            Bukkit.getOfflinePlayer(uuid).name
        }

        return ChatColor.translateAlternateColorCodes('&', nick!!)
    }


    fun setDisplayName(nick: String?) {
        val p = getPlayer() ?: return
        if (nick != null) {
            p.setDisplayName((getTier()?.color?: "").toString() + ChatColor.translateAlternateColorCodes('&', nick))
        } else {
            p.setDisplayName((getTier()?.color ?: "").toString() + p.name)
        }

    }


    fun getTier(): DonorTier? {
        val p = getPlayer() ?: return null
        return when {
            p.hasPermission("group.sapphire") -> DonorTier.SAPPHIRE
            p.hasPermission("group.amethyst") -> DonorTier.AMETHYST
            p.hasPermission("group.emerald") -> DonorTier.EMERALD
            p.hasPermission("group.topaz") -> DonorTier.TOPAZ
            p.hasPermission("group.ruby") -> DonorTier.RUBY
            else -> DonorTier.NONE

        }

    }


    fun setNickname(nick: String?, approved: Boolean = false) {
        nickname = nick

        setDisplayName(nick)

        val newStatus = if (approved) {
            NickStatus.APPROVED
        } else {
            NickStatus.PENDING
        }

        MySQL.executor.execute {
            try {
                MySQL.getConnection().use {
                    val stmt = it.prepareStatement("INSERT INTO player_nicks (PLAYER_ID, NICKNAME, STATUS, REQUESTED) VALUE ( ?, ?, ?, NULL) ON DUPLICATE KEY UPDATE NICKNAME = VALUES(NICKNAME), STATUS = VALUES(STATUS), REQUESTED = null ")
                    stmt.setInt(1, userID)
                    stmt.setString(2, nick)
                    stmt.setInt(3, newStatus.value)
                    stmt.executeUpdate()
                }
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }

    }

    fun getOfflinePlayer(): OfflinePlayer {
        return Bukkit.getOfflinePlayer(uuid)
    }

    fun setLastNick(name: String) {
        MySQL.executor.execute {
            MySQL.getConnection().use {conn ->
                conn.prepareStatement("UPDATE player_data SET PLAYER_LAST_NAME = ? WHERE PLAYER_ID = ?").use {stmt ->
                    stmt.setString(1, name)
                    stmt.setInt(2, userID)
                    stmt.executeUpdate()
                }
            }
        }
    }

}
