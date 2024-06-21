package com.example.coroutineflow_course.usecases.coroutines.usecase1.coroutine

import com.example.coroutineflow_course.mock.AndroidVersion
import com.example.coroutineflow_course.mock.MockApi
import com.example.coroutineflow_course.mock.VersionFeatures
import com.example.coroutineflow_course.mock.mockAndroidVersions
import com.example.coroutineflow_course.mock.mockVersionFeaturesAndroid10
import com.example.coroutineflow_course.mock.mockVersionFeaturesOreo
import com.example.coroutineflow_course.mock.mockVersionFeaturesPie

class FakeSuccessApi : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        return when(apiLevel) {
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("apiLevel not found")
        }
    }

}
