package de.dikodam.calendar

import de.dikodam.AbstractDay
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day01().executeTasks()
}

@ExperimentalTime
class Day01 : AbstractDay() {

    val input = readInputInts()

    override fun task1(): String {
        return input
            .zipWithNext()
            .count { (first, second) -> second > first }
            .toString()
    }

    override fun task2(): String {
        return input
//            .asSequence()
// sequence may have better memory performance / produce less garbage, but takes 14ms instead of 3ms
            .windowed(3)
            .map(List<Int>::sum)
            .zipWithNext()
            .count { (first, second) -> second > first }
            .toString()
    }
}
