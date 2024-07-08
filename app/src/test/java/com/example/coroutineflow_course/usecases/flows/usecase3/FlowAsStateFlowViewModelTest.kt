package com.example.coroutineflow_course.usecases.flows.usecase3

import com.example.coroutineflow_course.usecases.flows.utils.UiState
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlowAsStateFlowViewModelTest {

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should collect loading and success ui states on successful emissions`() = runTest {

        val fakeStockPriceDataSource = FakeStockPriceDataSource()
        val viewModel = FlowAsStateFlowUsecaseViewModel(fakeStockPriceDataSource)

        val collectJob =
            launch(UnconfinedTestDispatcher()) {
                viewModel.currentStockAsStateFlow.launchIn(this)
            }

        assertEquals(
            UiState.Loading,
            viewModel.currentStockAsStateFlow.value
        )

        fakeStockPriceDataSource.emit(
            listOf(
                googleStock,
                appleStock
            )
        )

        assertEquals(
            UiState.Success(
                listOf(
                    googleStock,
                    appleStock
                )
            ),
            viewModel.currentStockAsStateFlow.value
        )

        fakeStockPriceDataSource.emit(listOf(googleStock))

        assertEquals(
            UiState.Success(
                listOf(
                    googleStock
                )
            ),
            viewModel.currentStockAsStateFlow.value
        )

        collectJob.cancel()
    }

}