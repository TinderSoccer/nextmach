package com.nextmatch.app.data.repository

import android.util.Log
import com.nextmatch.app.data.entities.PlayerEntity
import com.nextmatch.app.data.remote.PlayerApiService
import com.nextmatch.app.data.remote.dto.PlayerDto
import com.nextmatch.app.utils.toEntity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest

class PlayerRepositoryTest : DescribeSpec({

    lateinit var apiService: PlayerApiService
    lateinit var repository: PlayerRepository

    beforeSpec {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    afterSpec {
        unmockkAll()
    }

    beforeTest {
        apiService = mockk()
        repository = PlayerRepository(apiService)
    }

    describe("refreshPlayers") {
        it("shouldEmitRemotePlayersWhenRefreshSucceeds") {
            runTest {
                val remotePlayers = listOf(sampleDto(id = "10"), sampleDto(id = "11"))
                coEvery { apiService.getPlayers(null) } returns remotePlayers

                repository.refreshPlayers()

                repository.players.value.shouldContainExactly(remotePlayers.map { it.toEntity() })
                coVerify(exactly = 1) { apiService.getPlayers(null) }
            }
        }

        it("shouldPropagateErrorWhenRefreshFails") {
            val expected = IllegalStateException("network down")
            coEvery { apiService.getPlayers(null) } throws expected

            runTest {
                shouldThrow<IllegalStateException> {
                    repository.refreshPlayers()
                }
            }
        }
    }

    describe("insertPlayer") {
        it("shouldAppendPlayerWhenInsertSucceeds") {
            runTest {
                val player = PlayerEntity(
                    id = "111",
                    nombre = "Alice",
                    correo = "alice@test.com",
                    posicion = "Portero",
                    nivel = "Intermedio",
                    telefono = "999",
                    equipoId = "team-a",
                    activo = true
                )
                val dtoResponse = sampleDto(id = player.id, nombre = player.nombre)
                coEvery { apiService.createPlayer(any()) } returns dtoResponse

                repository.insertPlayer(player)

                repository.players.value.shouldContainExactly(dtoResponse.toEntity())
            }
        }
    }

    describe("updatePlayer") {
        it("shouldReplacePlayerOnUpdate") {
            runTest {
                val original = sampleDto(id = "200", nombre = "Bob")
                coEvery { apiService.getPlayers(null) } returns listOf(original)
                repository.refreshPlayers()

            val updatedEntity = original.toEntity().copy(nombre = "Bobby")
            val updatedDto = original.copy(nombre = "Bobby")
            coEvery { apiService.updatePlayer(updatedEntity.id, any()) } returns updatedDto

                repository.updatePlayer(updatedEntity)

                repository.players.value.single().nombre shouldBe "Bobby"
            }
        }
    }

    describe("deletePlayer") {
        it("shouldRemovePlayerWhenDeleteSucceeds") {
            runTest {
                val existing = sampleDto(id = "300")
                coEvery { apiService.getPlayers(null) } returns listOf(existing)
                repository.refreshPlayers()

            coEvery { apiService.deletePlayer(existing.id!!) } returns Unit

                repository.deletePlayer(existing.toEntity())

                repository.players.value.shouldBeEmpty()
            }
        }
    }
})

private fun sampleDto(
    id: String,
    nombre: String = "John",
    correo: String = "john@test.com",
    posicion: String = "Delantero",
    nivel: String = "Avanzado",
    telefono: String = "123",
    equipoId: String = "team"
) = PlayerDto(
    id = id,
    nombre = nombre,
    correo = correo,
    posicion = posicion,
    nivel = nivel,
    telefono = telefono,
    equipoId = equipoId,
    activo = true
)
