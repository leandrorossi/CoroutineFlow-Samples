package com.example.coroutineflow_course.usecases.coroutines.usecase8

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DispatcherAndContextCoroutineViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performCalculation() should perform correct calculations`() = runTest {
        val viewModel =
            DispatcherAndContextCoroutineViewModel(StandardTestDispatcher(testScheduler))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

        viewModel.performCalculation(factorialOf = 5)
        advanceUntilIdle()

        Assert.assertEquals(
            UiState.Loading,
            receivedUiStates.first()
        )

        Assert.assertEquals(
            "120",
            (receivedUiStates[1] as UiState.Success).result
        )

    }

    private suspend fun DispatcherAndContextCoroutineViewModel.collect() {
        uiState.drop(1).collect { uiState ->
            receivedUiStates.add(uiState)
        }
    }

}