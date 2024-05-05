package com.example.coroutineflow_course.usecases.coroutines.usecase1.rxjava

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SequentialNetworkRequestsRxJavaViewModel(
    private val mockApi: RxMockApi = mockApi()
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private val disposables = CompositeDisposable()

    fun performToSequentialNetworkRequest() {
        _uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions()
            .flatMap { versions ->
                val recentVersion = versions.last()
                mockApi.getAndroidVersionFeatures(recentVersion.apiLevel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { features -> _uiState.value = UiState.Success(features) },
                onError = { _uiState.value = UiState.Error("Network request failed") }
            )
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}