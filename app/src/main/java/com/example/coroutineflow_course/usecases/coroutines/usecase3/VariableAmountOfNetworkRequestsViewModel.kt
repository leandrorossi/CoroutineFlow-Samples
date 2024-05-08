package com.example.coroutineflow_course.usecases.coroutines.usecase3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performNetworkRequestsSequentially() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                val versionFeatures = recentVersions.map {
                    mockApi.getAndroidVersionFeatures(it.apiLevel)
                }
                _uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                val versionFeatures = recentVersions.map {
                    async { mockApi.getAndroidVersionFeatures(it.apiLevel) }
                }.awaitAll()

                _uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }

    }

}
