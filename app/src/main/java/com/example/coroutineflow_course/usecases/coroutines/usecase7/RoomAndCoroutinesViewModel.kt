package com.example.coroutineflow_course.usecases.coroutines.usecase7

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomAndCoroutinesViewModel(
    private val mockApi: MockApi,
    private val database: AndroidVersionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun loadData() {
        _uiState.value = UiState.Loading.LoadFromDb

        viewModelScope.launch {
            delay(300)
            val localVersions = database.getAndroidVersions()

            if (localVersions.isEmpty()) {
                _uiState.value =
                    UiState.Error(dataSource = DataSource.DATABASE, message = "Database empty")
            } else {
                _uiState.value = UiState.Success(
                    dataSource = DataSource.DATABASE,
                    localVersions.mapToModelList()
                )
            }

            _uiState.value = UiState.Loading.LoadFromNetwork
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                database.insert(recentVersions.mapToEntity())
                _uiState.value = UiState.Success(
                    dataSource = DataSource.NETWORK,
                    androidVersions = recentVersions
                )
            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(dataSource = DataSource.NETWORK, message = "Something went wrong")
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            database.clear()
        }
    }

}

enum class DataSource(val dataSourceName: String) {
    DATABASE(dataSourceName = "Database"),
    NETWORK(dataSourceName = "Network")
}