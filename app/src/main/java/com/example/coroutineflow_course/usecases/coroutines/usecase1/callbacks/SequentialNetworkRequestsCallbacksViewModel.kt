package com.example.coroutineflow_course.usecases.coroutines.usecase1.callbacks

import androidx.lifecycle.ViewModel
import com.example.coroutineflow_course.mock.AndroidVersion
import com.example.coroutineflow_course.mock.VersionFeatures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private var getAndroidVersionCall: Call<List<AndroidVersion>>? = null
    private var getAndroidFeaturesCall: Call<VersionFeatures>? = null

    fun performSequentialNetworkRequest() {
        _uiState.value = UiState.Loading

        getAndroidVersionCall = mockApi.getRecentAndroidVersions()
        getAndroidVersionCall?.enqueue(object : Callback<List<AndroidVersion>> {
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if (response.isSuccessful) {
                    val mostRecentVersion = response.body()?.last()
                    getAndroidFeaturesCall = mostRecentVersion?.let {
                        mockApi.getAndroidVersionFeatures(it.apiLevel)
                    }

                    getAndroidFeaturesCall?.enqueue(object : Callback<VersionFeatures> {
                        override fun onResponse(
                            call: Call<VersionFeatures>,
                            response: Response<VersionFeatures>
                        ) {
                            if (response.isSuccessful) {
                                val featuresOfMostRecentVersion = response.body()
                                _uiState.value = featuresOfMostRecentVersion?.let {
                                    UiState.Success(it)
                                }!!
                            } else {
                                _uiState.value = UiState.Error("Network request failed")
                            }
                        }

                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            _uiState.value = UiState.Error("Something unexpected happened!")
                        }

                    })

                } else {
                    _uiState.value = UiState.Error("Network request failed")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                _uiState.value = UiState.Error("Something unexpected happened!")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        getAndroidVersionCall?.cancel()
        getAndroidFeaturesCall?.cancel()
    }

}