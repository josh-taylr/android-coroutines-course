package com.techyourchance.coroutines.exercises.exercise7

import com.techyourchance.coroutines.common.TestUtils
import com.techyourchance.coroutines.common.TestUtils.printCoroutineScopeInfo
import com.techyourchance.coroutines.common.TestUtils.printJobsHierarchy
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.lang.Exception
import kotlin.coroutines.EmptyCoroutineContext

class Exercise7Test {

    /*
    Write nested withContext blocks, explore the resulting Job's hierarchy, test cancellation
    of the outer scope
     */
    @Test
    fun nestedWithContext() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + Dispatchers.Default + CoroutineName("background-task"))
            val job = scope.launch {
                try {
                    delay(100) // computation
                    withContext(Dispatchers.IO + CoroutineName("network request")) {
                        try {
                            printJobsHierarchy(scopeJob)
                            delay(100) // request
                            println("network request complete")
                        } catch (e: CancellationException) {
                            println("network request cancelled")
                        }
                    }
                    println("background task complete")
                } catch (e: CancellationException) {
                    println("background task cancelled")
                }
            }
            scope.launch(CoroutineName("timeout")) {
                delay(250)
                println("cancelling background task")
                scope.cancel()
            }
            job.invokeOnCompletion { println("background task job complete") }
            job.join()
        }
    }

    /*
    Launch new coroutine inside another coroutine, explore the resulting Job's hierarchy, test cancellation
    of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedLaunchBuilders() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)

            scope.launch {
                try {
                    delay(100)
                    launch(CoroutineName("nested coroutine")) {
                        try {
                            printJobsHierarchy(scopeJob)
                            delay(100)
                            println("nested coroutine complete")
                        } catch (e: CancellationException) {
                            println("nested coroutine cancelled")
                        }
                    }
                    println("outer coroutine complete")
                } catch (e: CancellationException) {
                    println("outer coroutine cancelled")
                }
            }

            scope.launch {
                delay(150)
                scope.cancel()
            }

            scopeJob.join()
            println("test done")
        }
    }

    /*
    Launch new coroutine on "outer scope" inside another coroutine, explore the resulting Job's hierarchy,
    test cancellation of the outer scope, explore structured concurrency
     */
    @Test
    fun nestedCoroutineInOuterScope() {
        runBlocking {
            val scopeJob = Job()
            val scope = CoroutineScope(scopeJob + CoroutineName("outer scope") + Dispatchers.IO)

            scope.launch {
                try {
                    delay(100)
                    scope.launch(CoroutineName("nested coroutine")) {
                        try {
                            delay(100)
                            printJobsHierarchy(scopeJob)
                            println("nested coroutine complete")
                        } catch (e: CancellationException) {
                            println("nested coroutine cancelled")
                        }
                    }
                    println("outer coroutine complete")
                } catch (e: CancellationException) {
                    println("outer coroutine cancelled")
                }
            }

            scope.launch(CoroutineName("cancellation coroutine")) {
                delay(150)
                scope.cancel()
            }

            scopeJob.join()
            println("test done")
        }
    }


}