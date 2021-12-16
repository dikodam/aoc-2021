package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import kotlin.collections.ArrayDeque
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day11().executeTasks()
}

class Day11 : AbstractDay() {
    val input: Map<Coordinates2D, Octo> = readInputLines().map { line -> line.map { it.digitToInt() } }
        .flatMapIndexed { y, intLine -> intLine.mapIndexed { x, energy -> Coordinates2D(x, y) to Octo(energy) } }
        .toMap()

    override fun task1(): String {
        return ""
    }


    fun computeRound(state: Map<Coordinates2D, Octo>): Pair<Map<Coordinates2D, Octo>, Int> {
        val semiState = state.mapValues { (_, octo) -> octo.copy(energy = octo.energy + 1) }.toMutableMap()

        val visited = mutableSetOf<Octo>()
        val stack = ArrayDeque<Coordinates2D>()

        while (stack.isNotEmpty()) {
            // mark octo as hasFlashed
            // mark coords as visited

            // get neighbors that havent flashed yet
            // increase their energy by 1

            // if those neighbors are flashing, add them to stack

            // opt?: remove visited from stack
        }


        // reset flashOctos energy to 0
        val flashCount = semiState.getFlashingOctosCoords().count()
        val newState = semiState
            .mapValues { (_, octo) -> if (octo.hasFlashed) Octo(0) else octo }
        return newState to flashCount
    }

    fun Map<Coordinates2D, Octo>.getFlashingOctosCoords(): Set<Coordinates2D> {
        return this.filterValues { octo -> octo.energy > 9 }.keys
    }

    fun Coordinates2D.getNeighborsCoords(): List<Coordinates2D> {
        val left = this.copy(x = this.x - 1)
        val right = this.copy(x = this.x + 1)
        val up = this.copy(y = this.y - 1)
        val down = this.copy(y = this.y + 1)
        return listOf(left, right, up, down).filter { (x, y) -> x in 0..9 && y in 0..9 }
    }


    override fun task2(): String {
        return ""
    }
}

data class Octo(val energy: Int, val hasFlashed: Boolean = false) {
    fun isFlashing() = energy > 9
}