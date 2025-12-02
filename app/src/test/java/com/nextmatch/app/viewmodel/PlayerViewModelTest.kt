package com.nextmatch.app.viewmodel

import com.nextmatch.app.data.entities.PlayerEntity
import com.nextmatch.app.data.repository.PlayerRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModelTest : DescribeSpec({

    lateinit var repository: PlayerRepository
    lateinit var playersFlow: MutableStateFlow<List<PlayerEntity>>
    lateinit var viewModel: PlayerViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)

        playersFlow = MutableStateFlow(emptyList())
        repository = mockk(relaxed = true)
        every { repository.players } returns playersFlow
        coEvery { repository.refreshPlayers(any()) } returns Unit

        viewModel = PlayerViewModel(repository)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    describe("PlayerViewModel") {
        it("shouldExposePlayersFromRepositoryFlow") {
            val players = listOf(
                PlayerEntity(
                    id = "1",
                    nombre = "John",
                    correo = "john@test.com",
                    posicion = "Delantero",
                    nivel = "Avanzado",
                    telefono = "123456",
                    equipoId = "A",
                    activo = true
                )
            )

            playersFlow.value = players
            viewModel.uiState.value.players.shouldContainAll(players)
            viewModel.uiState.value.isLoading shouldBe false
            viewModel.uiState.value.error.shouldBeNull()
        }

        it("shouldCallRefreshWithTeamFilter") {
            val teamId = "team-1"

            viewModel.refreshPlayers(teamId)
            coVerify(exactly = 1) { repository.refreshPlayers(teamId) }
            viewModel.uiState.value.isLoading shouldBe false
            viewModel.uiState.value.error.shouldBeNull()
        }

        it("shouldSetErrorWhenRefreshFails") {
            val expected = IllegalStateException("network down")
            coEvery { repository.refreshPlayers("team-error") } throws expected

            viewModel.refreshPlayers("team-error")
            viewModel.uiState.value.error shouldBe expected.message
            viewModel.uiState.value.isLoading shouldBe false
        }

        it("shouldSetErrorWhenInsertFails") {
            val player = PlayerEntity(
                id = "2",
                nombre = "Paul",
                correo = null,
                posicion = null,
                nivel = null,
                telefono = null,
                equipoId = null,
                activo = true
            )
            val expected = IllegalStateException("insert failed")
            coEvery { repository.insertPlayer(player) } throws expected

            viewModel.insertPlayer(player)
            coVerify { repository.insertPlayer(player) }
            viewModel.uiState.value.error shouldBe expected.message
            viewModel.uiState.value.isLoading shouldBe false
        }

        it("shouldRefreshAfterSuccessfulUpdateAndPreserveFilter") {
            val filteredTeam = "team-21"
            val player = PlayerEntity(
                id = "3",
                nombre = "Mario",
                correo = null,
                posicion = "Defensa",
                nivel = "Intermedio",
                telefono = "555",
                equipoId = filteredTeam,
                activo = true
            )
            viewModel.refreshPlayers(filteredTeam)

            viewModel.updatePlayer(player)

            coVerify { repository.updatePlayer(player) }
            coVerify { repository.refreshPlayers(filteredTeam) }
            viewModel.uiState.value.error.shouldBeNull()
        }

        it("shouldSetErrorWhenUpdateFails") {
            val player = PlayerEntity(
                id = "4",
                nombre = "Chris",
                correo = null,
                posicion = null,
                nivel = null,
                telefono = null,
                equipoId = null,
                activo = true
            )
            val expected = IllegalStateException("update failed")
            coEvery { repository.updatePlayer(player) } throws expected

            viewModel.updatePlayer(player)

            coVerify { repository.updatePlayer(player) }
            viewModel.uiState.value.error shouldBe expected.message
            viewModel.uiState.value.isLoading shouldBe false
        }

        it("shouldSetErrorWhenDeleteFails") {
            val player = PlayerEntity(
                id = "5",
                nombre = "Mike",
                correo = null,
                posicion = null,
                nivel = null,
                telefono = null,
                equipoId = null,
                activo = true
            )
            val expected = IllegalStateException("delete failed")
            coEvery { repository.deletePlayer(player) } throws expected

            viewModel.deletePlayer(player)

            coVerify { repository.deletePlayer(player) }
            viewModel.uiState.value.error shouldBe expected.message
            viewModel.uiState.value.isLoading shouldBe false
        }
    }
})
