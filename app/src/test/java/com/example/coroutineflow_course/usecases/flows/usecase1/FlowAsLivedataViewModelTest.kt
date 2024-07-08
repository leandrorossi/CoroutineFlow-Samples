package com.example.coroutineflow_course.usecases.flows.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coroutineflow_course.usecases.flows.utils.UiState
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class FlowAsLivedataViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return success UI States with values of fakeDataSource`() = runTest {

        val fakeDataSource = FakeStockPriceDataSource()
        val viewModel = FlowAsLivedataUsecaseViewModel(fakeDataSource)
        observeViewModel(viewModel)

        assertEquals(UiState.Loading, receivedUiStates[0])
        assertEquals(1, receivedUiStates.size)

        fakeDataSource.emit(listOf(googleStock, appleStock))
        assertEquals(UiState.Success(listOf(googleStock, appleStock)), receivedUiStates[1])
        assertEquals(2, receivedUiStates.size)

        fakeDataSource.emit(listOf(googleStock))
        assertEquals(UiState.Success(listOf(googleStock)), receivedUiStates[2])
        assertEquals(3, receivedUiStates.size)
    }

    private fun observeViewModel(viewModel: FlowAsLivedataUsecaseViewModel) {
        viewModel.currentStockAsLivedata.observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }

}