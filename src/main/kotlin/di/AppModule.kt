package dev.stranik.di

import dev.stranik.controller.AuthController
import dev.stranik.data.repository.UserRepositoryImpl
import dev.stranik.domain.repository.UserRepository
import dev.stranik.domain.usecases.LoginUseCase
import dev.stranik.domain.usecases.RegisterUseCase
import dev.stranik.security.PasswordHasher
import io.ktor.server.application.Application

object AppContainer {
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val loginUseCase: LoginUseCase by lazy { LoginUseCase(userRepository, PasswordHasher) }
    val registerUseCase: RegisterUseCase by lazy { RegisterUseCase(userRepository, PasswordHasher) }
    /*val getUserUseCase: GetUserUseCase by lazy { GetUserUseCase(userRepository) }
    val getUserCountriesUseCase: GetUserCountriesUseCase by lazy { GetUserCountriesUseCase(userRepository) }*/

    val authController: AuthController by lazy { AuthController(loginUseCase, registerUseCase, PasswordHasher) }
    /*val userController: UserController by lazy { UserController(getUserUseCase, getUserCountriesUseCase) }*/
}

fun Application.appModule() {
    println("DI инициализирован")
}