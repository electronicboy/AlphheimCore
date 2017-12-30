/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package pw.alphheim.alphheimplugin.data

import pw.alphheim.alphheimplugin.utils.MySQL
import java.sql.Statement
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by shane on 8/25/16.
 */
class AlphheimUser(uuid: UUID, isNPC: Boolean = false ) {

    private var userID: Int = -1
    private var activeChannel: String? = null
    private var channels: Map<Any, Any> = HashMap()


    init {

        MySQL.getConnection().use { connection ->

            val prepareStatement = connection.prepareStatement("SELECT PLAYER_ID FROM player_data WHERE PLAYER_UUID = ?");
            prepareStatement.use { statement ->
                statement.setString(1, uuid.toString())
                if (statement.execute()) {
                    val resultSet = statement.resultSet
                    resultSet.use { rs ->
                        userID = rs.getInt(rs.findColumn("PLAYER_ID"))
                    }
                } else {
                    val statement1 = connection.prepareStatement("INSERT INTO player_data (PLAYER_UUID) VALUES (?)", Statement.RETURN_GENERATED_KEYS)
                    statement1.use { insertStatement -> {
                        insertStatement.setString(1, uuid.toString())
                        insertStatement.executeUpdate()
                        userID = insertStatement.generatedKeys.getInt(1)
                    }}

                }
            }

        }


    }


}
