/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.logging.Log
import org.flywaydb.core.api.logging.LogCreator
import org.flywaydb.core.api.logging.LogFactory
import pw.valaria.aperture.ApertureCore
import java.sql.Connection
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.flywaydb.core.internal.logging.javautil.JavaUtilLogCreator
import org.flywaydb.core.api.logging.LogFactory.setLogCreator



object MySQL {

    private lateinit var dataSource: HikariDataSource
    val executor = Executors.newCachedThreadPool(com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("MySQL Executor Thread - %1\$d").build())!!

    @Suppress("UNUSED_PARAMETER")
    fun init(plugin: ApertureCore) {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/vcore"
        config.username = "valaria"
        config.password = "9dmfpae22"
        // We have this info, might as well use it
        LogFactory.setLogCreator(JavaUtilLogCreator())
        val load = Flyway.configure().dataSource(config.jdbcUrl, config.username, config.password).load()

        load.migrate();

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