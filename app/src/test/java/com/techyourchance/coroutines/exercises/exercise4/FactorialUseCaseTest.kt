package com.techyourchance.coroutines.exercises.exercise4

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

class FactorialUseCaseTest {

    private lateinit var SUT: FactorialUseCase

    @Before
    fun setup() {
        SUT = FactorialUseCase()
    }

    @Test
    fun computeFactorial_0_returns1() {
        runBlocking {
            // Arrange
            // Act
            val result = SUT.computeFactorial(0, 1000)
            // Assert
            assertThat((result as FactorialUseCase.Result.Success).result, `is`(BigInteger("1")))
        }
    }

    @Test
    fun computeFactorial_1_returns1() {
        runBlocking {
            // Arrange
            // Act
            val result = SUT.computeFactorial(1, 1000)
            // Assert
            assertThat((result as FactorialUseCase.Result.Success).result, `is`(BigInteger("1")))
        }
    }

    @Test
    fun computeFactorial_10_returnsCorrectAnswer() {
        runBlocking {
            // Arrange
            // Act
            val result = SUT.computeFactorial(10, 1000)
            // Assert
            assertThat((result as FactorialUseCase.Result.Success).result, `is`(BigInteger("3628800")))
        }
    }

    @Test
    fun computeFactorial_30_returnsCorrectAnswer() {
        runBlocking {
            // Arrange
            // Act
            val result = SUT.computeFactorial(30, 1000)
            // Assert
            assertThat(
                (result as FactorialUseCase.Result.Success).result,
                `is`(BigInteger("265252859812191058636308480000000"))
            )
        }
    }
}