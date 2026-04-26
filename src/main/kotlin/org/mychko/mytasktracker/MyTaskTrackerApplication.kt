package org.mychko.mytasktracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class]
) class MyTaskTrackerApplication

@RestController
class HelloController {
    @GetMapping("/")
    fun hello() = "Hello World2!"
}

fun main(args: Array<String>) {
    runApplication<MyTaskTrackerApplication>(*args)
}