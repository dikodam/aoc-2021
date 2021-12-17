package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day15().executeTasks()
}

data class Node(val position: Coordinates2D, val cost: Int)

class Day15 : AbstractDay() {

    // TODO parse input as 2D grid, pack into Nodes
    // drop (0,0) because it's the start and shouldn't be counted?
    // add both (?) first neighbors (0,1) and (1,0) to queue with their respective distance as priority
    // TODO can i alter priority of an entry in the queue?
    //  i guess not, but you could remove it and reinsert it with a different priority


    override fun task1(): String {
        val riskLevel: Map<Coordinates2D, Int> = readInputLines().flatMapIndexed { y, line ->
            line.mapIndexed() { x, numberChar -> Coordinates2D(x, y) to numberChar.digitToInt() }
        }.toMap()

        val costToTarget: MutableMap<Coordinates2D, Int> =
            riskLevel.keys.associateWith { Integer.MAX_VALUE }.toMutableMap()

        costToTarget[Coordinates2D(0, 0)] = 0 // don't count risk value of (0,0)

        val queue = PriorityQueue<Node>()
        sequenceOf(Coordinates2D(0, 1), Coordinates2D(1, 0))
            .forEach { coords -> queue.add(Node(coords, riskLevel[coords]!!)) }

        while (queue.isNotEmpty()) {
            val (coorsd, newCost) = queue.remove()


//            coords.getNeighbors()
//                .filter{} // nru die die existieren + sinnvoll? sind
//                .forEach{neighbor -> queue.offer(Node(neighbor, currentCost + riskLevel[neighbor]))}
        }

        return ""
    }

    override fun task2(): String {
        return ""
    }
}