package com.example.coroutineflow_course.usecases.coroutines.usecase4

import com.example.coroutineflow_course.mock.AndroidVersion
import com.example.coroutineflow_course.mock.MockApi
import com.example.coroutineflow_course.mock.VersionFeatures
import com.example.coroutineflow_course.mock.mockAndroidVersions
import com.example.coroutineflow_course.utils.EndpointShouldNotBeCalledException
import kotlinx.coroutines.delay

class FakeSuccessApi(private val responseDelay: Long) : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        delay(responseDelay)
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }

}