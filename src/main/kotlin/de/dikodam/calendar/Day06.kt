package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day06().executeTasks()
}

val memoizationMap = mutableMapOf<Pair<Int, Int>, Long>()
fun computeRes(startState: Int, rounds: Int): Long {
    if (startState > rounds) {
        return 1L
    }
    val key = startState to rounds
    if (memoizationMap.keys.contains(key)) {
        return memoizationMap[key]!!
    }

    var count = 1L
    var currentState = startState
    var remainingRounds = rounds
    while (remainingRounds > 0) {
        currentState -= 1
        if (currentState == -1) {
            currentState = 6
            count += computeRes(8, remainingRounds - 1)
        }
        remainingRounds -= 1
    }
    memoizationMap[key] = count
    return count
}

class Day06 : AbstractDay() {
    //    val input: List<Lanternfish> = "3,4,3,1,2"
    val input: List<Lanternfish> = readInputRaw()
        .split(",").map { Lanternfish(it.toInt()) }


    override fun task1(): String {
        var lanternfishes = input
        repeat(80) {
            lanternfishes = lanternfishes.flatMap { it.tick() }
        }
        return lanternfishes.count().toString()
    }

    override fun task2(): String {
        val fishCounter = mutableMapOf<Int, Long>()
        readInputRaw()
            //"3,4,3,1,2"
            .split(",")
            .map { it.toInt() }
            .groupBy { it }
            .mapValuesTo(fishCounter) { (_, fishList) -> fishList.size.toLong() }

        repeat(256) {
            // index shift by -1
            // index 0 goes to index 6 AND another copy to index 8
            // index 6 is sum of index 0 and index 7
            var buffer = fishCounter[0] ?: 0L
            fishCounter[0] = fishCounter[1] ?: 0L
            fishCounter[1] = fishCounter[2] ?: 0L
            fishCounter[2] = fishCounter[3] ?: 0L
            fishCounter[3] = fishCounter[4] ?: 0L
            fishCounter[4] = fishCounter[5] ?: 0L
            fishCounter[5] = fishCounter[6] ?: 0L
            fishCounter[6] = (fishCounter[7] ?: 0L) + (buffer)
            fishCounter[7] = fishCounter[8] ?: 0L
            fishCounter[8] = buffer
        }

        return fishCounter.values.sum().toString()
    }


}

data class Lanternfish(val counter: Int) {
    fun tick(): List<Lanternfish> {
        val newCounter = counter.dec()
        return if (newCounter == -1) {
            listOf(Lanternfish(6), Lanternfish(8))
        } else {
            listOf(Lanternfish(newCounter))
        }
    }
}

