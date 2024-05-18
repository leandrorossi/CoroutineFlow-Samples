package com.example.coroutineflow_course.usecases.coroutines.usecase10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coroutineflow_course.mock.MockApi

class ViewModelFactory(private val repository: AndroidVersionRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AndroidVersionRepository::class.java)
            .newInstance(repository)
    }
}