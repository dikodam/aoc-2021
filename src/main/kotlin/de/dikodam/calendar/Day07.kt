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
    override fun task1(): String {
        val min = input.minOf { it }
        val max = input.maxOf { it }

        var fuelConsumption = Int.MAX_VALUE
        for (m in min..max) {
            val newFuelConsm = input.sumOf { (it - m).absoluteValue }
            if (newFuelConsm < fuelConsumption) {
                fuelConsumption = newFuelConsm
            }
        }

        return fuelConsumption.toString()
    }

    override fun task2(): String {
        val min = input.minOf { it }
        val max = input.maxOf { it }

        var fuelConsumption = Int.MAX_VALUE
        for (m in min..max) {
            val newFuelConsm = input
                .map { (it - m).absoluteValue }
                .map { distance -> (0..distance).sumOf { it } }
                .sumOf { it }
            if (newFuelConsm < fuelConsumption) {
                fuelConsumption = newFuelConsm
            }
        }

        return fuelConsumption.toString()
    }

}