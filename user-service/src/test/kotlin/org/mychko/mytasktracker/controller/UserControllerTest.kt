package org.mychko.mytasktracker.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mychko.mytasktracker.model.User
import org.mychko.mytasktracker.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime


@WebMvcTest(UserController::class)
 class UserControllerTest {

 @MockkBean
 private lateinit var userService: UserService

 @Autowired
 private lateinit var mockMvc: MockMvc

 @Autowired
 private lateinit var objectMapper: ObjectMapper

@Test
@DisplayName("Should create new user")
 fun create() {

 val userRequest = User(username = "vladimir", email = "vladimir@example.com")
 val userResponse = User(id = 1L, username = "vladimir", email = "vladimir@example.com", isActive = true, createdAt = LocalDateTime.now())

 every { userService.create(any()) } returns userResponse

 mockMvc.perform(post("/users")
  .contentType(MediaType.APPLICATION_JSON)
  .content(objectMapper.writeValueAsString(userRequest)))
  .andExpect(status().isCreated)
  .andExpect(jsonPath("$.id").value(1L))
  .andExpect(jsonPath("$.username").value("vladimir"))
  .andExpect(jsonPath("$.email").value("vladimir@example.com"))
}

@Test
@DisplayName("Should return all users")
 fun getAll() {
 val users = listOf(
  User(id = 1L, username = "vladimir", email = "vladimir@example.com"),
  User(id = 2L, username = "dmitriy", email = "dmitriy@example.com")
 )

 every { userService.getAll() } returns users

 mockMvc.perform(get("/users"))
  .andExpect(status().isOk)
  .andExpect(jsonPath("$[0].id").value(1L))
  .andExpect(jsonPath("$[0].username").value("vladimir"))
  .andExpect(jsonPath("$[0].email").value("vladimir@example.com"))
  .andExpect(jsonPath("$[1].id").value(2L))
  .andExpect(jsonPath("$[1].username").value("dmitriy"))
  .andExpect(jsonPath("$[1].email").value("dmitriy@example.com"))
  .andExpect(jsonPath("$.length()").value(2))
}

@Test
@DisplayName("Should return user by id")
 fun getById() {
 val userId = 8L
 val user = User(id = userId, username = "vladimir", email = "vladimir@example.com")

 every { userService.getById(userId) } returns user

 mockMvc.perform(get("/users/$userId"))
  .andExpect(status().isOk)
  .andExpect(jsonPath("$.username").value("vladimir"))
  .andExpect(jsonPath("$.email").value("vladimir@example.com"))
}

@Test
@DisplayName("Should update user by id")
 fun update() {
  val userId = 8L
  val createdAt = LocalDateTime.of(2025, 1, 1, 12, 0)
  val userRequest = User(username = "vladimirUpdated", email = "vladimirUpdated@example.com", isActive = false)
  val userResponse = User(id = userId, username = "vladimirUpdated", email = "vladimirUpdated@example.com", isActive = false, createdAt = createdAt)

  every { userService.update(eq(userId), any())} returns userResponse

   mockMvc.perform(put("/users/$userId")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(userRequest)))
    .andExpect(status().isOk)
    .andExpect(jsonPath("$.id").value(userId))
    .andExpect(jsonPath("$.username").value("vladimirUpdated"))
    .andExpect(jsonPath("$.email").value("vladimirUpdated@example.com"))
    .andExpect(jsonPath("$.isActive").value(false))

 verify(exactly = 1) {
  userService.update(userId, any())
 }
 }

@Test
@DisplayName("Should delete user by id")
 fun delete() {
  val userId = 8L
  every { userService.delete(userId) } just Runs

 mockMvc.perform(delete("/users/${userId}"))
  .andExpect(status().isNoContent)
 }
}