package dev.stranik.data.databases

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        // val jdbcUrl = env("DB_JDBC_URL", "jdbc:postgresql://localhost:5432/musicapp")
        val jdbcUrl = env("DB_JDBC_URL", "jdbc:postgresql://musicapp.stranik.dev:5434/musicapp")
        val username = env("DB_USERNAME", "musicapp")
        val password = env("DB_PASSWORD", "very_secure_password_123123d")
        val driverClassName = env("DB_DRIVER", "org.postgresql.Driver")

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.driverClassName = driverClassName
            this.username = username
            this.password = password
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

    private fun env(name: String, defaultValue: String): String {
        val value = System.getenv(name)
        return if (value.isNullOrBlank()) defaultValue else value
    }
}