package com.example.coroutineflow_course.usecases.coroutines.usecase12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class CoroutineExceptionHandlingViewModel(
    private val mockApi: MockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun handleExceptionWithTryCatch() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                mockApi.getAndroidVersionFeatures(27)

            } catch (e: Exception) {
                _uiState.value = UiState.Error("Network Request failed: ${e.message}")
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        _uiState.value = UiState.Loading

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            _uiState.value = UiState.Error("Network Request failed!! ${exception.message}")
        }

        viewModelScope.launch(exceptionHandler) {
            mockApi.getAndroidVersionFeatures(27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {

            supervisorScope {
                val oreoFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { mockApi.getAndroidVersionFeatures(29) }

                val versionFeatures =
                    listOf(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)
                        .mapNotNull {
                            try {
                                it.await()
                            } catch (e: Exception) {
                                if (e is CancellationException) {
                                    throw e
                                }
                                null
                            }
                        }

                _uiState.value = UiState.Success(versionFeatures)
            }
        }

    }

}