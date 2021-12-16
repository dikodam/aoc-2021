package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day12().executeTasks()
}

const val start = "start"
const val end = "end"

class Day12 : AbstractDay() {

    val testInput1 = """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".trim().lines()
    val testInput2 = """dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc""".trim().lines()
    val testInput3 = """fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW""".trim().lines()

    val adjacencyList: List<Pair<String, String>> =
//        testInput1 // 36 check
//        testInput2 //  103 check
//        testInput3 //  3509
        readInputStrings()
            .buildAdjacencyList()


    fun List<String>.buildAdjacencyList(): List<Pair<String, String>> {
        return this.map { it.split("-") }
            .map { (parent, child) -> parent to child }
            .flatMap { (left, right) -> listOf(left to right, right to left) }
    }

    override fun task1(): String {
        val paths = dfs(start, adjacencyList, currentPath = listOf(start), visited = setOf(start))
        return "${paths.size}"
    }

    fun String.isSmallCave() =
        this.all { it.isLowerCase() }

    fun dfs(
        currentNode: String,
        adjacencyList: List<Pair<String, String>>,
        currentPath: List<String>,
        visited: Set<String>
    ): List<List<String>> {
        if (currentNode == end) {
            return listOf(currentPath)
        }

        val children = adjacencyList.getNeighbors(currentNode)
            .filterNot { it in visited }

        return if (children.isEmpty()) {
            emptyList()
        } else {
            val newVisited = if (currentNode.isSmallCave()) visited + currentNode else visited
            children.flatMap { child -> dfs(child, adjacencyList, currentPath + child, newVisited) }
        }
    }

    fun List<Pair<String, String>>.getNeighbors(parent: String): List<String> {
        return this.filter { (parentNode, _) -> parent == parentNode }
            .map { it.second }
    }


    override fun task2(): String {
        val paths = dfs2(
            currentPath = listOf(start),
            adjacencyList
        )

        return "${paths.size}"
    }

    fun dfs2(currentPath: List<String>, adjacencyList: List<Pair<String, String>>): List<List<String>> {
        val currentNode = currentPath.last()
        if (currentNode == end) {
            return listOf(currentPath)
        }

        val wasSmallCaveVisitedTwice = currentPath
            .filterNot { it == start }
            .filter { it.isSmallCave() }
            .groupingBy { it }
            .eachCount()
            .any { (_, count) -> count == 2 }

        val nonVisitableCaves = if (wasSmallCaveVisitedTwice) {
            currentPath.filter { it.isSmallCave() }
        } else {
            listOf(start)
        }

        val neighbors = adjacencyList.getNeighbors(currentNode)
            .filterNot { it in nonVisitableCaves }

        return if (neighbors.isEmpty()) {
            emptyList()
        } else {
            neighbors.flatMap { child -> dfs2(currentPath + child, adjacencyList) }
        }
    }

}