package com.example.coroutineflow_course.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coroutineflow_course.mock.mockVersionFeaturesAndroid10
import com.example.coroutineflow_course.mock.mockVersionFeaturesOreo
import com.example.coroutineflow_course.mock.mockVersionFeaturesPie
import com.example.coroutineflow_course.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConcurrentNetworkRequestsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequestsSequentially should return data after 3 times the response delay`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = ConcurrentNetworkRequestsViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequestsSequentially()
            advanceUntilIdle()

            assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )

            assertEquals(
                3000,
                currentTime
            )

        }

    @Test
    fun `performNetworkRequestsConcurrently should return data after the response delay`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = ConcurrentNetworkRequestsViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequestsConcurrently()
            advanceUntilIdle()

            assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )

            assertEquals(
                1000,
                currentTime
            )

        }

    @Test
    fun `performNetworkRequestsConcurrently should return Error when network request fails`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = ConcurrentNetworkRequestsViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequestsConcurrently()
            advanceUntilIdle()

            assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network request failed")
                ),
                receivedUiStates
            )
        }

    private suspend fun ConcurrentNetworkRequestsViewModel.collect() {
        uiState.drop(1).collect { uiState ->
            receivedUiStates.add(uiState)
        }
    }

}