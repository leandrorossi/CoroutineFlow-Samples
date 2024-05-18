package com.example.coroutineflow_course.usecases.coroutines.usecase10

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coroutineflow_course.mock.AndroidVersion

@Entity(tableName = "android_versions")
data class AndroidVersionEntity(@PrimaryKey val apiLevel: Int, val name: String)

fun List<AndroidVersionEntity>.mapToModelList() = map {
    AndroidVersion(it.apiLevel, it.name)
}

fun List<AndroidVersion>.mapToEntity() = map { AndroidVersionEntity(it.apiLevel, it.name) }