package org.mychko.mytasktracker

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceApplicationTests {

    @Test
    @Disabled("Исключен из запуска: требует реальную БД. Для тестирования контроллеров используются изолированные @WebMvcTest")
    fun contextLoads() {
    }

}
