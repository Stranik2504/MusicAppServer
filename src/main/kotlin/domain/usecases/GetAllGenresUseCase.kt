package dev.stranik.domain.usecases

import dev.stranik.domain.repository.GenresRepository

class GetAllGenresUseCase(
    private val genresRepository: GenresRepository
) {
    suspend operator fun invoke() = genresRepository.findAll()
}