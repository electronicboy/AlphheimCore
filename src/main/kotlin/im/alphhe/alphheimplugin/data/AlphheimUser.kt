/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.data

import im.alphhe.alphheimplugin.components.chat.ChatStatus
import im.alphhe.alphheimplugin.utils.MySQL
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.sql.Statement
import java.util.*

/**
 * Created by shane on 8/25/16.
 */
class AlphheimUser(val uuid: UUID, @Suppress("UNUSED_PARAMETER") isNPC: Boolean = false) {

    private var userID: Int = -1
    var nickname: String? = null
        get() {
            return field ?: getPlayer()?.displayName
        }

    var activeChannel: String? = null
    var channelData: Map<String, ChatStatus> = mutableMapOf();

    private fun getPlayer(): Player? {
        return Bukkit.getPlayer(uuid)
    }

    init {
        println("creating user...")

        MySQL.getConnection().use { connection ->
            println("open conn")
            val prepareStatement = connection.prepareStatement("SELECT PLAYER_ID FROM player_data WHERE PLAYER_UUID = ?");
            println("got prep")
            prepareStatement.use { statement ->
                println("using prep")
                statement.setString(1, uuid.toString())
                if (statement.execute()) {
                    println("using prep1")
                    val resultSet = statement.resultSet

                    resultSet.use { rs ->
                        println("rs1 use")
                        if (rs.next()) {
                            println("rs next")
                            userID = rs.getInt(rs.findColumn("PLAYER_ID"))
                        }
                        println("finuse")
                    }
                }
            }

            println("check + $userID")

        }
        if (userID == -1) {

            MySQL.getConnection().use { connection ->
                val statement1 = connection.prepareStatement("INSERT INTO player_data (PLAYER_UUID) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
                println("stmt2")
                statement1.use { insertStatement ->

                    println("2nd statement")
                    insertStatement.setString(1, uuid.toString())
                    insertStatement.executeUpdate()
                    println("sount")

                    insertStatement.use {
                        it.generatedKeys.use {
                            if (it.next())
                                userID = it.getInt(1)
                        }
                    }
                }
            }
        }
    }

}
