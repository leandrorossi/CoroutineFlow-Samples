package com.example.coroutineflow_course.usecases.coroutines.usecase10

import com.example.coroutineflow_course.mock.mockAndroidVersions

class FakeDatabase : AndroidVersionDao {

    var insertedIntoDb = false

    override suspend fun getAndroidVersions(): List<AndroidVersionEntity> {
        return mockAndroidVersions.mapToEntity()
    }

    override suspend fun insert(androidVersionEntity: List<AndroidVersionEntity>) {
        insertedIntoDb = true
    }

    override suspend fun clear() {}

}