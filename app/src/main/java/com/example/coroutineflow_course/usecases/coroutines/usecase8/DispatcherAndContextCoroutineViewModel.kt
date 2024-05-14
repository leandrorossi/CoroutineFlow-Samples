package com.example.coroutineflow_course.usecases.coroutines.usecase8

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class DispatcherAndContextCoroutineViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performCalculation(factorialOf: Int) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                var result: BigInteger
                val computationDuration = measureTimeMillis {
                    result = calculateFactorialOf(factorialOf)
                }

                var resultString: String
                val stringConversionDuration = measureTimeMillis {
                    resultString = convertToString(result)
                }

                _uiState.value =
                    UiState.Success(resultString, computationDuration, stringConversionDuration)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error while calculating factorial")
            }
        }
    }

    private suspend fun calculateFactorialOf(number: Int): BigInteger =
        withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) {
            number.toString()
        }

}