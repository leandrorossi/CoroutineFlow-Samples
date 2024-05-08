package com.example.coroutineflow_course.usecases.coroutines.usecase2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConcurrentNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performNetworkRequestsSequentially() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                _uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(message = "Network request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        _uiState.value = UiState.Loading

        val oreoFeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(27)
        }

        val pieFeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(28)
        }

        val android10FeaturesDeferred = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(29)
        }

        viewModelScope.launch {
            try {
                val versionFeatures =
                    awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)
                _uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(message = "Network request failed")
            }
        }
    }

}