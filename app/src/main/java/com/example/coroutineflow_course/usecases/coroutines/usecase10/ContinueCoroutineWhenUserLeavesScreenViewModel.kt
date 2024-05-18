package com.example.coroutineflow_course.usecases.coroutines.usecase10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContinueCoroutineWhenUserLeavesScreenViewModel(
    private val repository: AndroidVersionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun loadData() {
        _uiState.value = UiState.Loading.LoadFromDb

        viewModelScope.launch {
            delay(300)
            val localVersions = repository.getLocalAndroidVersions()

            if (localVersions.isEmpty()) {
                _uiState.value =
                    UiState.Error(dataSource = DataSource.DATABASE, message = "Database empty")
            } else {
                _uiState.value = UiState.Success(
                    dataSource = DataSource.DATABASE,
                    androidVersions = localVersions
                )
            }

            _uiState.value = UiState.Loading.LoadFromNetwork

            try {
                _uiState.value = UiState.Success(
                    dataSource = DataSource.NETWORK,
                    androidVersions = repository.loadAndStoreRemoteAndroidVersions()
                )
            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(dataSource = DataSource.NETWORK, message = "Something went wrong")
            }
        }
    }

    fun clearDatabase() {
        repository.clearDatabase()
    }

}

enum class DataSource(val dataSourceName: String) {
    DATABASE(dataSourceName = "Database"),
    NETWORK(dataSourceName = "Network")
}