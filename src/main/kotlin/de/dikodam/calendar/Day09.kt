package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day09().executeTasks()
}

class Day09 : AbstractDay() {

    val input: Map<Coordinates2D, Int> = readInputStrings()
        /*
        val input: Map<Coordinates2D, Int> = """2199943210
    3987894921
    9856789892
    8767896789
    9899965678""".lines()
    */
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

    fun getNeighborsCoords(point: Coordinates2D, map: Map<Coordinates2D, Int>): List<Coordinates2D> {
        val up = point.copy(x = point.x - 1)
        val down = point.copy(x = point.x + 1)
        val left = point.copy(y = point.y - 1)
        val right = point.copy(y = point.y + 1)
        return listOf(up, down, left, right)
            .filter { spot -> spot in map.keys }
    }

    override fun task2(): String {
        // identify basins by breadth first search
        // for each point,
        //  if registered, skip
        //  if not registered, register
        //   if ==9 skip else start new basin:
        // getNeighbors
        // filterNonRegistered Neighbors
        // register each != 9 as part current basin
        // flatMap each != 9 neighbor to its neighbors

        val basins = mutableListOf<Basin>()
        val visitedPoints = mutableSetOf<Coordinates2D>()
        for (point in input.keys) {
            if (point in visitedPoints) continue
            visitedPoints += point
            if (input[point] == 9) continue
            val currentBasin = mutableSetOf<Coordinates2D>()

            val stack = ArrayDeque<Coordinates2D>()
            stack += getNeighborsCoords(point, input)

            while (stack.isNotEmpty()) {
                val currentPoint = stack.remove()
                if (currentPoint in visitedPoints) continue
                visitedPoints += currentPoint
                if (input[currentPoint] == 9) continue
                currentBasin += currentPoint
                stack += getNeighborsCoords(currentPoint, input)
            }
            basins.add(currentBasin)
        }

        val result = basins.map { it.size }
            .sortedDescending()
            .take(3)
            .map { it + 1 }
            .reduce(Int::times)
        return "$result"
    }
}

typealias Basin = Set<Coordinates2D>
