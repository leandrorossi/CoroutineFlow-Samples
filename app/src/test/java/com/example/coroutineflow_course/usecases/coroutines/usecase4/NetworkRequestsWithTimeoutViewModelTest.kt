package com.example.coroutineflow_course.usecases.coroutines.usecase4

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coroutineflow_course.mock.mockAndroidVersions
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkRequestsWithTimeoutViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequest() should return Success UiState on successful network request within timeout`() =
        runTest {
            val responseDelay = 1000L
            val timeout = 1001L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = NetworkRequestsWithTimeoutViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequest(timeout)
            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receivedUiStates
            )

        }

    @Test
    fun `performNetworkRequest() should return Error UiState with timeout error message if timeout gets exceeded`() =
        runTest {
            val responseDelay = 1000L
            val timeout = 999L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = NetworkRequestsWithTimeoutViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequest(timeout)
            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network request timed out")
                ),
                receivedUiStates
            )

        }

    @Test
    fun `performNetworkRequest() should return Error UiState on unsuccessful network response`() =
        runTest {
            val responseDelay = 1000L
            val timeout = 1001L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = NetworkRequestsWithTimeoutViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequest(timeout)
            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network request failed")
                ),
                receivedUiStates
            )

        }

    private suspend fun NetworkRequestsWithTimeoutViewModel.collect() {
        uiState.drop(1).collect { uiState ->
            receivedUiStates.add(uiState)
        }
    }

}