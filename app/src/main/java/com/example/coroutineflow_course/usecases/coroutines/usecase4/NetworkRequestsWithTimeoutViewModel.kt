package com.example.coroutineflow_course.usecases.coroutines.usecase4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class NetworkRequestsWithTimeoutViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performNetworkRequest(timeout: Long) {
        _uiState.value = UiState.Loading

        // usingWithTimeOut(timeout)
        usingWithTimeOutOrNull(timeout)
    }

    private fun usingWithTimeOut(timeout: Long) {
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeout(timeout) {
                    mockApi.getRecentAndroidVersions()
                }

                _uiState.value = UiState.Success(recentAndroidVersions)
            } catch (timeout: TimeoutCancellationException) {
                _uiState.value = UiState.Error("Network request timed out")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }
    }

    private fun usingWithTimeOutOrNull(timeout: Long) {
        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeoutOrNull(timeout) {
                    mockApi.getRecentAndroidVersions()
                }

                if (recentAndroidVersions != null) {
                    _uiState.value = UiState.Success(recentAndroidVersions)
                } else {
                    _uiState.value = UiState.Error("Network request timed out")
                }
            } catch (timeout: TimeoutCancellationException) {
                _uiState.value = UiState.Error("Network request timed out")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }
    }

}