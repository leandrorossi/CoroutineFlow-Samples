package com.example.coroutineflow_course.usecases.coroutines.usecase5

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NetworkRequestsWithRetryViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performNetworkRequest() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                retry(numberOfRetries = 3) {
                    loadRecentAndroidVersions()
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }

    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Log.e("retryException", "retry: ", e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
        return block()
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = mockApi.getRecentAndroidVersions()
        _uiState.value = UiState.Success(recentAndroidVersions)
    }

}