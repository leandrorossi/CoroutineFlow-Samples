package com.example.coroutineflow_course.usecases.coroutines.usecase1.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SequentialNetworkRequestsCoroutineViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performSequentialNetworkRequest() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                val mostRecentVersion = recentVersions.last()
                val featuresOfMostRecentVersion = mockApi.getAndroidVersionFeatures(mostRecentVersion.apiLevel)
                _uiState.value = UiState.Success(featuresOfMostRecentVersion)
            } catch (ex: Exception) {
                _uiState.value = UiState.Error("Network request failed")
            }
        }
    }

}