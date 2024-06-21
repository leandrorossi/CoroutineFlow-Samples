package com.example.coroutineflow_course.usecases.coroutines.usecase5

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coroutineflow_course.mock.mockAndroidVersions
import com.example.coroutineflow_course.usecases.coroutines.usecase4.NetworkRequestsWithTimeoutViewModel
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkRequestsWithRetryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performSingleNetworkRequest() should return Success UiState on successful network response`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = NetworkRequestsWithRetryViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequest()
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
        fun `performSingleNetworkRequest() should retry network request two times`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessOnThirdAttemptApi(responseDelay)
            val viewModel = NetworkRequestsWithRetryViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequest()
            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                3,
                fakeApi.requestCount
            )

            // 3*1000 (Request delays) + 100 (initial delay) + 200 (second delay)
            Assert.assertEquals(
                3300,
                currentTime
            )

        }

    @Test
    fun `performSingleNetworkRequest() should return Error UiState on 3 unsuccessful network responses`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = NetworkRequestsWithRetryViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequest()
            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network request failed")
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                3,
                fakeApi.requestCount
            )

            // 3*1000 response delays + 100 (initial delay) + 200 (second delay)
            Assert.assertEquals(
                3300,
                currentTime
            )

        }

    private suspend fun NetworkRequestsWithRetryViewModel.collect() {
        uiState.drop(1).collect { uiState ->
            receivedUiStates.add(uiState)
        }
    }

}