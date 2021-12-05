package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day05().executeTasks()
}

class Day05 : AbstractDay() {

        val input = readInputStrings()
//    val input = """0,9 -> 5,9
//8,0 -> 0,8
//9,4 -> 3,4
//2,2 -> 2,1
//7,0 -> 7,4
//6,4 -> 2,0
//0,9 -> 2,9
//3,4 -> 1,4
//0,0 -> 8,8
//5,5 -> 8,2""".lines()

    override fun task1(): String {
        val lineDefs = input
            .map { parsetoLineDefinition(it) }
            .filter { it.isHorizontalOrVertical() }

        return lineDefs
            .flatMap { it.toPoints() }
            .groupBy { it }
            .mapValues { (_, pointList) -> pointList.size }
            .filterValues { pointCount -> pointCount > 1 }
            .count()
            .toString()
    }

    fun parsetoLineDefinition(line: String): LineDefinition {
        val (startPoint, endPoint) = line.split(" -> ")
            .map { numbersString ->
                val (leftNumber, rightNumber) = numbersString.split(",").map { it.toInt() }
                Point(leftNumber, rightNumber)
            }
        return LineDefinition(startPoint, endPoint)
    }


    override fun task2(): String {
        return input
            .map { parsetoLineDefinition(it) }
            .flatMap { it.toPoints() }
            .groupBy { it }
            .mapValues { (_, pointList) -> pointList.size }
            .filterValues { pointCount -> pointCount > 1 }
            .count()
            .toString()
    }
}

data class LineDefinition(val start: Point, val end: Point) {

    fun isHorizontalOrVertical(): Boolean =
        start.x == end.x || start.y == end.y

    fun toPoints(): List<Point> {
        fun makeRange(start: Int, end: Int): IntRange =
            if (start < end) {
                start..end
            } else {
                end..start
            }

        if (start.x == end.x) {
            return makeRange(start.y, end.y)
                .map { y -> Point(start.x, y) }
        }
        if (start.y == end.y) {
            return makeRange(start.x, end.x)
                .map { x -> Point(x, start.y) }
        }
        return makeDiagonalPoints()
    }

    fun makeDiagonalPoints(): List<Point> {
        val xSeq = if (start.x < end.x) (start.x..end.x) else start.x downTo end.x
        val ySeq = if (start.y < end.y) (start.y..end.y) else start.y downTo end.y
        return xSeq.toList()
            .zip(ySeq.toList())
            .map { (x, y) -> Point(x, y) }
    }
}

data class Point(val x: Int, val y: Int) {

}

