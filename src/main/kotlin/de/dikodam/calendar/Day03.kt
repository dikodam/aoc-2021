package de.dikodam.calendar

import de.dikodam.AbstractDay
import java.lang.StringBuilder
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day03().executeTasks()
}

@ExperimentalTime
class Day03 : AbstractDay() {

    val input = readInputStrings()

    override fun task1(): String {
        val length = input[0].length
        val sb = StringBuilder()
        for (index in 0 until length) {
            val numbersAtPos = input.map { it[index] }
                .map { it.digitToInt() }
            val count1 = numbersAtPos.count { it == 1 }
            val count0 = numbersAtPos.count { it == 0 }
            if (count1 > count0) sb.append(1) else sb.append(0)
        }
        val gamma = sb.toString().toInt(2)
        val epsilon = gamma xor "1".repeat(length).toInt(2)
        return (gamma * epsilon).toString()

    }

    fun filterMostCommonBit(lines: List<String>, pos: Int, fallback: Int): Int {
        val numbers = lines.map { it[pos].digitToInt() }
        val count1 = numbers.count { it == 1 }
        val count0 = numbers.count { it == 0 }
        if (count1 == count0) {
            return fallback
        }
        return if (count1 > count0) 1 else 0

    }

    override fun task2(): String {
        val ogr = findOxygenGeneratorRating(input)
        val co2s = findCo2Scrubbing(input)
        return "${ogr * co2s}"
    }

    fun findOxygenGeneratorRating(input: List<String>): Int {
        var list = input.map { it }
        val length = input[0].length
        for (pos in 0 until length) {
            list = list.filter { it[pos].digitToInt() == filterMostCommonBit(list, pos, 1) }
            if (list.size == 1) {
                return list[0].toInt(2)
            }
        }
        error("something went wrong")
    }

    fun findCo2Scrubbing(input: List<String>): Int {
        var list = input.map { it }
        val length = input[0].length
        for (pos in 0 until length) {
            list = list.filter { it[pos].digitToInt() != filterMostCommonBit(list, pos, 1) }
            if (list.size == 1) {
                return list[0].toInt(2)
            }
        }
        error("something went wrong")
    }

}