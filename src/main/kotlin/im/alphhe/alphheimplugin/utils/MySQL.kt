/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object MySQL {

    private var dataSource: HikariDataSource


    init {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/alphheim"
        config.username = "alphheim"
        config.password = "S92ns902nas"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        dataSource = HikariDataSource(config)
    }

    fun getConnection(): Connection {
        return dataSource.connection

    }




}