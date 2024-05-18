package com.example.coroutineflow_course.usecases.coroutines.usecase10

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AndroidVersionDao {

    @Query("SELECT * FROM android_versions")
    suspend fun getAndroidVersions(): List<AndroidVersionEntity>

    @Upsert
    suspend fun insert(androidVersionEntity: List<AndroidVersionEntity>)

    @Query("DELETE FROM android_versions")
    suspend fun clear()

}