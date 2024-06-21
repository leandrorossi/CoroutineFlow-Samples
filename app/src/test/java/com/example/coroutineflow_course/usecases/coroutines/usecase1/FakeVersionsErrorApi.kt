package com.example.coroutineflow_course.usecases.coroutines.usecase1.coroutine

import com.example.coroutineflow_course.mock.AndroidVersion
import com.example.coroutineflow_course.mock.MockApi
import com.example.coroutineflow_course.mock.VersionFeatures
import com.example.coroutineflow_course.utils.EndpointShouldNotBeCalledException
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeVersionsErrorApi : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        throw HttpException(
            Response.error<List<AndroidVersion>>(
                500,
                ResponseBody.create(MediaType.parse("application/json"), "")
            )
        )
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }

}