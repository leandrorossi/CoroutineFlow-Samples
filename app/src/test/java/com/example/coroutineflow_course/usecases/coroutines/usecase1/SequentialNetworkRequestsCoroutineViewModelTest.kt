package com.example.coroutineflow_course.usecases.coroutines.usecase1.coroutine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coroutineflow_course.mock.mockVersionFeaturesAndroid10
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SequentialNetworkRequestsCoroutineViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    private val receiveUiStates = mutableListOf<UiState>()

    @Test
    fun `should return Success when both network requests are successful`() = runTest {
        val fakeApi = FakeSuccessApi()
        val viewModel = SequentialNetworkRequestsCoroutineViewModel(fakeApi)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

        viewModel.performSequentialNetworkRequest()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockVersionFeaturesAndroid10)
            ),
            receiveUiStates
        )

    }

    @Test
    fun `should return Error when first network requests fails`() = runTest {
        val fakeApi = FakeVersionsErrorApi()
        val viewModel = SequentialNetworkRequestsCoroutineViewModel(fakeApi)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

        viewModel.performSequentialNetworkRequest()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed")
            ),
            receiveUiStates
        )

    }

    @Test
    fun `should return Error when second network requests fails`() = runTest {
        val fakeApi = FakeFeaturesErrorApi()
        val viewModel = SequentialNetworkRequestsCoroutineViewModel(fakeApi)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

        viewModel.performSequentialNetworkRequest()

        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network request failed")
            ),
            receiveUiStates
        )

    }

    private suspend fun SequentialNetworkRequestsCoroutineViewModel.collect() {
        uiState.drop(1).collect { uiState ->
            receiveUiStates.add(uiState)
        }
    }

}