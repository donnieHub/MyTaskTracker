package simulations

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.*
import java.time.Duration

/**
 * Нагрузочный тест PATCH /api/users/{id} — проверяет:
 *  - partial update через UserPatchRequest
 *  - поведение при конфликте уникального email (DataIntegrityViolationException -> 409)
 *  - что Kafka-событие (user-events) не блокирует и не замедляет ответ API под нагрузкой
 *
 */
class UserServiceSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl("http://localhost:8081")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")
        .userAgentHeader("GatlingLoadTest")

    // CSV с колонками: userId,email
    // Пример строки: 1,user1@example.com
    private val userFeeder = csv("users.csv").circular()

    private val patchUserScenario = scenario("Patch existing user")
        .feed(userFeeder)
        .exec(
            http("PATCH /users/{id} - update email")
                .patch("/users/#{userId}")
                .body(StringBody("""{ "email": "updated-#{userId}-#{randomInt()}@example.com" }"""))
                .check(status().shouldBe(200))
                .check(jsonPath("$.email").exists())
        )
        .pause(Duration.ofMillis(200), Duration.ofMillis(800)) // имитация "думающего" клиента

    // Отдельный сценарий: намеренно бьём в один и тот же email,
    // чтобы проверить корректность обработки DataIntegrityViolationException под конкурентной нагрузкой
    private val conflictingEmailScenario = scenario("Patch with duplicate email (conflict check)")
        .feed(userFeeder)
        .exec(
            http("PATCH /users/{id} - duplicate email")
                .patch("/users/#{userId}")
                .body(StringBody("""{ "email": "shared-conflict-email@example.com" }"""))
                .check(status().`in`(200, 409)) // допускаем оба исхода, интересует именно НЕ 500
        )

    init {
        setUp(
            patchUserScenario.injectOpen(
                rampUsers(10).during(Duration.ofSeconds(10)),
                constantUsersPerSec(20.0).during(Duration.ofSeconds(30)),
                rampUsersPerSec(20.0).to(100.0).during(Duration.ofSeconds(30))
            ),
            conflictingEmailScenario.injectOpen(
                rampUsers(5).during(Duration.ofSeconds(10)),
                constantUsersPerSec(5.0).during(Duration.ofSeconds(20))
            )
        )
            .protocols(httpProtocol)
            .assertions(
                global().responseTime().percentile3().lt(500), // p95 < 500ms
                global().failedRequests().percent().lt(1.0)     // < 1% ошибок (кроме ожидаемых 409)
            )
    }
}