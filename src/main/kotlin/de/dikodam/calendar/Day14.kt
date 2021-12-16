package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day14().executeTasks()
}


class Day14 : AbstractDay() {

    val inputLines =
        readInputLines()
//        """NNCB
//
//CH -> B
//HH -> N
//CB -> H
//NH -> C
//HB -> C
//HC -> B
//HN -> C
//NN -> C
//BH -> H
//NC -> B
//NB -> B
//BN -> B
//BB -> N
//BC -> B
//CC -> N
//CN -> C""".trim().lines()

    val initialState = inputLines[0]
    val mappings: Map<String, Char> = inputLines
        .drop(2)
        .map { it.split(" -> ") }
        .associate { (charPair, insertChar) -> charPair to insertChar[0] }

    override fun task1(): String {
        var polymer = initialState
        repeat(10) {
            polymer = polymer.expandPolymer(mappings)
        }
        val countedChars = polymer.groupingBy { it }.eachCount()
        val maxCount = countedChars.maxOf { it.value }
        val minCount = countedChars.minOf { it.value }
        return "${maxCount - minCount}"
    }

    fun String.expandPolymer(mapping: Map<String, Char>): String {
        return this.windowed(size = 2, partialWindows = true)
            .map { charPair -> if (mapping.keys.contains(charPair)) "${charPair[0]}${mapping[charPair]}" else charPair[0] }
            .joinToString(separator = "")
    }


    override fun task2(): String {
        val polymer = initialState
        var polymerStore = polymer.windowed(2)
            .associateWith { 1L }
        repeat(40) {
            polymerStore = polymerStore.expandPolymers()
        }
        val charCounts = polymerStore.countChars()
            .also { map -> map.merge(polymer.last(), 1L, Long::plus) }
        val maxCount = charCounts.values.maxOf { it }
        val minCount = charCounts.values.minOf { it }
        return "${maxCount - minCount}"
    }

    fun Map<String, Long>.expandPolymers(): Map<String, Long> {
        val result: MutableMap<String, Long> = mutableMapOf()
        for ((str, count) in this) {
            val midChar = mappings[str]!!
            result.merge("${str[0]}$midChar", count, Long::plus)
            result.merge("$midChar${str[1]}", count, Long::plus)
        }
        return result
    }

    fun Map<String, Long>.countChars(): MutableMap<Char, Long> {
        val result: MutableMap<Char, Long> = mutableMapOf()
        for ((str, count) in this) {
            val char1 = str[0]
            result.merge(char1, count, Long::plus)
        }
        return result
    }

}