package com.example.coroutineflow_course.usecases.coroutines.usecase9

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

class FactorialCalculator(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun calculateFactorial(factorialOf: Int, numberOfCoroutines: Int): BigInteger {

        return withContext(defaultDispatcher) {
            val subRanges = createSubRangeList(factorialOf, numberOfCoroutines)

            subRanges.map {
                async { calculateFactorialOfSubRange(it) }
            }.awaitAll()
                .fold(BigInteger.ONE) { acc, next -> acc.multiply(next) }
        }
    }

    fun calculateFactorialOfSubRange(subRange: SubRange): BigInteger {
        var factorial = BigInteger.ONE
        for (i in subRange.start..subRange.end) {
            factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        }
        return factorial
    }

    fun createSubRangeList(factorialOf: Int, numberOfSubRanges: Int): List<SubRange> {
        val quotient = factorialOf.div(numberOfSubRanges)
        val rangeList = mutableListOf<SubRange>()

        var curStartIndex = 1
        repeat(numberOfSubRanges - 1) {
            rangeList.add(
                SubRange(
                    start = curStartIndex,
                    end = curStartIndex + (quotient - 1)
                )
            )
            curStartIndex += quotient
        }
        rangeList.add(SubRange(start = curStartIndex, end = factorialOf))
        return rangeList
    }

}

data class SubRange(val start: Int, val end: Int)