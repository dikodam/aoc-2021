package de.dikodam

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
abstract class AbstractDay {

    fun executeTasks() {
        val day = this::class.simpleName

        val (result1, duration1) = measureTimedValue { task1() }
        val (result2, duration2) = measureTimedValue { task2() }

        println("The result of $day task 1 is <$result1> and took ${duration1.inWholeMilliseconds}ms to compute")
        println("The result of $day task 2 is <$result2> and took ${duration2.inWholeMilliseconds}ms to compute")
    }

    abstract fun task1(): String
    abstract fun task2(): String

    private fun findDayFile(): File {
        val day = this::class.simpleName
        return File("src/main/resources/$day.txt")
    }

    fun readInputRaw(): String {
        return findDayFile().readText()
    }

    fun readInputStrings(): List<String> {
        return findDayFile().readLines()
    }

    fun readInputInts(): List<Int> {
        return readInputStrings()
            .map { it.toInt() }
    }

}