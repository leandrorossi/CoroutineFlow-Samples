package com.example.coroutineflow_course.usecases.coroutines.usecase6

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class NetworkRequestsWithTimeoutAndRetryViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performNetworkRequest() {
        _uiState.value = UiState.Loading

        val numberOfRetries = 2
        val timeout = 1000L

        viewModelScope.launch {
            try {
                val oreoVersionFeaturesDeferred = async {
                    retry(numberOfRetries) {
                        withTimeout(timeout) {
                            mockApi.getAndroidVersionFeatures(27)
                        }
                    }
                }

                val pieVersionFeaturesDeferred = async {
                    retryWithTimeout(numberOfRetries = 2, timeout = 1000) {
                        mockApi.getAndroidVersionFeatures(28)
                    }
                }

                val versionFeatures =
                    awaitAll(oreoVersionFeaturesDeferred, pieVersionFeaturesDeferred)
                _uiState.value = UiState.Success(versionFeatures)

            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }
    }

    private suspend fun <T> retryWithTimeout(
        numberOfRetries: Int,
        timeout: Long,
        block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Log.e("retryException", "retry: ", e)
            }
            delay(delayBetweenRetries)
        }
        return block()
    }

}