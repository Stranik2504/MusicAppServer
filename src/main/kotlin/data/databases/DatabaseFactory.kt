package dev.stranik.data.databases

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        // TODO: Move config into file config
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://ep-sweet-shape-amf5qpex.c-5.us-east-1.aws.neon.tech/neondb?sslmode=require"
            driverClassName = "org.postgresql.Driver"
            username = "neondb_owner"
            password = "npg_QSIye73LDbpV"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                UserTable,
                ArtistsTable,
                GenresTable,
                AlbumsTable,
                TracksTable,
                PlaylistsTable,
                PlaylistTracksTable,
                FollowedArtistsTable,
                LikedTracksTable,
                ListeningHistoryTable
            )
        }

        println("PostgreSQL подключён успешно")
    }
}