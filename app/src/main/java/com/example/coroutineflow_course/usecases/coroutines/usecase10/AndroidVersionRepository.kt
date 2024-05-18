package com.example.coroutineflow_course.usecases.coroutines.usecase10

import com.example.coroutineflow_course.mock.AndroidVersion
import com.example.coroutineflow_course.mock.MockApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AndroidVersionRepository(
    private val database: AndroidVersionDao,
    private val scope: CoroutineScope,
    private val mockApi: MockApi = mockApi()
) {

    suspend fun getLocalAndroidVersions(): List<AndroidVersion> {
        return database.getAndroidVersions().mapToModelList()
    }

    suspend fun loadAndStoreRemoteAndroidVersions(): List<AndroidVersion> {

        return scope.async {
            val recentVersions = mockApi.getRecentAndroidVersions()

            database.insert(recentVersions.mapToEntity())

            recentVersions
        }.await()

    }

    fun clearDatabase() {
        scope.launch {
            database.clear()
        }
    }

}