package com.example.coroutineflow_course.usecases.coroutines.usecase3

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
class VariableAmountOfNetworkRequestsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequestsSequentially() should return Success UiState on successful network requests after 4000ms`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

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
                4000,
                currentTime
            )

        }

    @Test
    fun `performNetworkRequestsSequentially() should return Error UiState on unsuccessful recent-android-versions network request`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeVersionsErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequestsSequentially()
            advanceUntilIdle()

            assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network request failed")
                ),
                receivedUiStates
            )

        }

    @Test
    fun `performNetworkRequestsSequentially() should return Error UiState on unsuccessful android-version-features network request`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeFeaturesErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.collect() }

            viewModel.performNetworkRequestsSequentially()
            advanceUntilIdle()

            assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network request failed")
                ),
                receivedUiStates
            )

        }

    @Test
    fun `performNetworkRequestsConcurrently() should return Success UiState on successful network requests after 2000ms`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

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
                2000,
                currentTime
            )

        }

    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on unsuccessful recent-android-versions network request`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeVersionsErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

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

    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on unsuccessful android-version-features network request`() =
        runTest {
            val responseDelay = 1000L
            val fakeApi = FakeFeaturesErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)

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

    private suspend fun VariableAmountOfNetworkRequestsViewModel.collect() {
        uiState.drop(1).collect { uiState ->
            receivedUiStates.add(uiState)
        }
    }

}