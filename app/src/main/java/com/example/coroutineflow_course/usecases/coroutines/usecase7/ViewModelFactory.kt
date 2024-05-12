package com.example.coroutineflow_course.usecases.coroutines.usecase7

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coroutineflow_course.mock.MockApi

class ViewModelFactory(private val mockApi: MockApi, private val database: AndroidVersionDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MockApi::class.java, AndroidVersionDao::class.java)
            .newInstance(mockApi, database)
    }
}