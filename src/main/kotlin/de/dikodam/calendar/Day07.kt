package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day07().executeTasks()
}

class Day07 : AbstractDay() {
    val input = readInputRaw().split(",").map(String::toInt)
    val min = input.minOf { it }
    val max = input.maxOf { it }
    override fun task1(): String {
        return (min..max)
            .map { m -> input.sumOf { (it - m).absoluteValue } }
            .minOf { it }
            .toString()
    }

    override fun task2(): String {
        return (min..max)
            .map { m ->
                input
                    .map { (it - m).absoluteValue }
                    .map { sumUpTo(it) }
                    .sumOf { it }
            }
            .minOf { it }
            .toString()
    }
}

fun sumUpTo(x: Int) =
    (x * (x + 1)) / 2 // little gauss
//    (0..x).sumOf { it }