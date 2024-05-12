package com.example.coroutineflow_course.usecases.coroutines.usecase7

import com.example.coroutineflow_course.mock.MockNetworkInterceptor
import com.example.coroutineflow_course.mock.createMockApi
import com.example.coroutineflow_course.mock.mockAndroidVersions
import com.google.gson.Gson

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            { Gson().toJson(mockAndroidVersions) },
            200,
            1000
        )
)