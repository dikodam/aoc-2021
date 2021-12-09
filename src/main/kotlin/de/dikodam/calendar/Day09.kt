package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day09().executeTasks()
}

class Day09 : AbstractDay() {

    val input: Map<Coordinates2D, Int> = readInputStrings()
        .flatMapIndexed { y, line ->
            line.asSequence()
                .mapIndexed { x, i -> Coordinates2D(x, y) to i.digitToInt() }
        }
        .toMap()

    override fun task1(): String {
        return input.map { (coord, i) -> i to getNeighbors(coord, input) }
            .filter { (spot, neighbors) -> neighbors.all { n -> n > spot } }
            .map { (spot, _) -> spot + 1 }
            .sumOf { it }
            .toString()
    }

    fun getNeighbors(point: Coordinates2D, map: Map<Coordinates2D, Int>): List<Int> {
        val up = point.copy(x = point.x - 1)
        val down = point.copy(x = point.x + 1)
        val left = point.copy(y = point.y - 1)
        val right = point.copy(y = point.y + 1)
        return listOf(up, down, left, right)
            .mapNotNull { map[it] }
    }

    override fun task2(): String {
        return ""
    }

}
