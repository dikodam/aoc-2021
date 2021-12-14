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
//        readInputStrings()
        """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C""".trim().lines()

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
        var polymer = initialState

        var pairCounts: Map<String, Long> = polymer.windowed(2)
            .associateWith { 0L }
            .toMutableMap()

        repeat(40) {
            pairCounts = pairCounts.expandPolymers(mappings)
        }

        val charCount = pairCounts.asSequence()
            .flatMap { (chars, count) ->
                generateSequence { chars[0] }
                    .take(count) +
                        generateSequence { chars[1] }
                            .take(count)
            }
            .groupingBy { it }
            .eachCount()


        return "x"
    }

    fun Map<String, Char>.expandFromKey(key: String): List<String> {
        return if (key !in this.keys) {
            listOf(key)
        } else {
            val char = this[key]
            listOf("${key[0]}$char", "$char${key[1]}")
        }
    }

    fun Map<String, Int>.expandPolymers(mapping: Map<String, Char>): Map<String, Int> {
        return this.flatMap { (polymerKey, _) -> mapping.expandFromKey(polymerKey) }
            .groupBy { it }
            .mapValues { (_, expansionList) -> expansionList.size }
    }
}