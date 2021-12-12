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
        testInput1 // 54 is wrong, should be 36
//        testInput2 // 247 is wrong, should be 103
//        testInput3 // 212945 is wrong, should be 3509
//        readInputStrings()
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

        val children = adjacencyList.getChildren(currentNode)
            .filterNot { it in visited }

        return if (children.isEmpty()) {
            emptyList()
        } else {
            val newVisited = if (currentNode.isSmallCave()) visited + currentNode else visited
            children.flatMap { child -> dfs(child, adjacencyList, currentPath + child, newVisited) }
        }
    }

    fun List<Pair<String, String>>.getChildren(parent: String): List<String> {
        return this.filter { (parentNode, _) -> parent == parentNode }
            .map { it.second }
    }

    override fun task2(): String {
        val paths = dfs2(
            start,
            adjacencyList,
            currentPath = listOf(start),
            visited = mapOf(start to 1)
        )
        return "${paths.size}"
    }

    fun dfs2(
        currentNode: String,
        adjacencyList: List<Pair<String, String>>,
        currentPath: List<String>,
        visited: Map<String, Int>
    ): List<List<String>> {
        if (currentNode == end) {
            return listOf(currentPath)
        }
        var children = adjacencyList.getChildren(currentNode)
            .filterNot { it == start }

        val hasSmallCaveBeenVisited = visited.values.contains(2)
        if (hasSmallCaveBeenVisited) {
            children = children.filterNot { it.isSmallCave() }
        }
        return if (children.isEmpty()) {
            emptyList()
        } else {
            val newVisited = visited.computeNewVisted(currentNode)
            children.flatMap { child -> dfs2(child, adjacencyList, currentPath + child, newVisited) }
        }
    }

    fun Map<String, Int>.computeNewVisted(currentNode: String): Map<String, Int> {
        return if (!currentNode.isSmallCave() || currentNode == start) {
            this
        } else {
            this.map { (key, value) -> if (key == currentNode) key to value + 1 else key to value }
                .toMap()
        }
    }

}