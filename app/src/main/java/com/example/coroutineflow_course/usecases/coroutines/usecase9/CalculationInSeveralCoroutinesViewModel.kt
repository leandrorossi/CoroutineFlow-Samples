package com.example.coroutineflow_course.usecases.coroutines.usecase9

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

class CalculationInSeveralCoroutinesViewModel(private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default) :
    ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    fun performCalculation(factorialOf: Int, numberOfCoroutines: Int) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            var factorialResult: BigInteger
            val computationDuration = measureTimeMillis {
                factorialResult =
                    FactorialCalculator().calculateFactorial(factorialOf, numberOfCoroutines)
            }

            var resultString: String
            val stringConversionDuration = measureTimeMillis {
                resultString = convertToString(factorialResult)
            }

            _uiState.value =
                UiState.Success(resultString, computationDuration, stringConversionDuration)
        }
    }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) { number.toString() }

}