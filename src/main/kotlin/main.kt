package dev.stranik

import dev.stranik.Routing.configureRouting
import dev.stranik.data.databases.DatabaseFactory
import dev.stranik.di.appModule
import dev.stranik.plugins.configureAuthentication
import dev.stranik.plugins.configureCORS
import dev.stranik.plugins.configureCallLogging
import dev.stranik.plugins.configureContentNegotiation
import dev.stranik.plugins.configureStatusPages
import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    /*install(OpenApi) {
        info {
            title = "My API"
            version = "1.0.0"
        }
        server {
            url = "http://localhost:8080"
            description = "Локальный сервер"
        }
        security {
            securityScheme("MyJwtAuth") {
                type = AuthType.HTTP
                scheme = AuthScheme.BEARER
                bearerFormat = "JWT"
            }
        }
    }*/
    DatabaseFactory.init()
    /*transaction {
        if (UserTable.selectAll().where { UserTable.username eq "admin" }.empty()) {
            UserTable.insert {
                it[username] = "admin"
                it[passwordHash] = PasswordHasher.hash("secret123")
                it[role] = "admin"
            }
        }
    }
    transaction {
        CountryTable.insert {
            it[userId] = 1
            it[name] = "Россия"
            it[code] = "RU"
        }
        CountryTable.insert {
            it[userId] = 1
            it[name] = "США"
            it[code] = "US"
        }
        CountryTable.insert {
            it[userId] = 1
            it[name] = "Германия"
            it[code] = "DE"
        }
        CountryTable.insert {
            it[userId] = 1
            it[name] = "Франция"
            it[code] = "FR"
        }
    }*/
    appModule()
    configureContentNegotiation()
    configureCORS()
    configureCallLogging()
    configureStatusPages()
    configureAuthentication()
    configureRouting()
}
