/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import im.alphhe.alphheimplugin.EladriaCore
import java.sql.Connection
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object MySQL {

    private lateinit var dataSource: HikariDataSource
    val executor = Executors.newCachedThreadPool(com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("MySQL Executor Thread - %1\$d").build())!!

    @Suppress("UNUSED_PARAMETER")
    fun init(plugin: EladriaCore) {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/alphheim"
        config.username = "alphheim"
        config.password = "S92ns902nas"
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        config.addDataSourceProperty("useSSL", "false")
        dataSource = HikariDataSource(config)
    }

    init {

    }

    fun getConnection(): Connection {
        return dataSource.connection

    }

    fun kill() {
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)
        dataSource.close()
    }


}