package com.example.coroutineflow_course

import android.app.Application
import com.example.coroutineflow_course.usecases.coroutines.usecase10.AndroidVersionRepository
import com.example.coroutineflow_course.usecases.coroutines.usecase10.AndroidVersionDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val androidVersionRepository by lazy {
        val database = AndroidVersionDatabase.getInstance(applicationContext).androidVersionDao()
        AndroidVersionRepository(database, applicationScope)
    }

}