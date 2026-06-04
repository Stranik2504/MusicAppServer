package dev.stranik

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.*

class ServerTest {

    @Test
    fun `test ping endpoint`() = testApplication {
        application {
            module()
        }

        assertEquals(HttpStatusCode.OK, client.get("/ping").status)
    }

}
