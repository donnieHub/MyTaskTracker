package org.mychko.mytasktracker

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Disabled("Исключен из запуска: требует реальную БД. Для тестирования контроллеров используются изолированные @WebMvcTest")
class UserServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
