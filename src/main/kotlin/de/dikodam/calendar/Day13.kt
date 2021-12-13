package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import java.lang.StringBuilder
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day13().executeTasks()
}

data class FoldingInstruction(val scale: Char, val position: Int)

class Day13 : AbstractDay() {

    val pointsAndFoldingInstructions: Pair<List<Coordinates2D>, List<FoldingInstruction>> =
        parseInput(readInputStrings())

    fun parseInput(rawInputLines: List<String>): Pair<List<Coordinates2D>, List<FoldingInstruction>> {
        val points: List<Coordinates2D> = rawInputLines
            .takeWhile { it.trim().isNotBlank() }
            .map { it.split(",") }
            .map { (left, right) -> Coordinates2D(left.toInt(), right.toInt()) }

        val foldingInstructions = rawInputLines
            .drop(points.size + 1)
            .map { it.substring(11).split("=") }
            .map { (left, right) -> FoldingInstruction(left[0], right.toInt()) }

        return points to foldingInstructions

    }

    override fun task1(): String {
        val (points, foldingInstructions) = pointsAndFoldingInstructions
        var pointSet = points.toSet()
        val instruction = foldingInstructions[0]
        pointSet = foldAsInstructed(pointSet, instruction)
        return "${pointSet.size}"
    }


    fun foldAsInstructed(points: Set<Coordinates2D>, foldingInstruction: FoldingInstruction): Set<Coordinates2D> {
        val (axis, position) = foldingInstruction
        val splitPointsAlongAxis = if (axis == 'x') {
            { (x, _): Coordinates2D -> x < position }
        } else {
            { (_, y): Coordinates2D -> y < position }
        }
        val (pointsToKeep, pointsToFold) = points.partition(splitPointsAlongAxis)

        val foldPoint = if (axis == 'x') {
            { (x, y): Coordinates2D -> Coordinates2D(2 * position - x, y) }
        } else {
            { (x, y): Coordinates2D -> Coordinates2D(x, 2 * position - y) }
        }
        val foldedPoints = pointsToFold.map(foldPoint)
        return (pointsToKeep + foldedPoints).toSet()
    }

    override fun task2(): String {

        val (points, foldingInstructions) = pointsAndFoldingInstructions
        var pointSet = points.toSet()
        val finalPoints = foldingInstructions.fold(pointSet, ::foldAsInstructed)
        visualize(finalPoints)
        return ""
    }

    private fun visualize(finalPoints: Set<Coordinates2D>) {
        val maxX = finalPoints.maxOf { it.x }
        val maxY = finalPoints.maxOf { it.y }


        val sb = StringBuilder()

        for (x in 0..maxX) {
            for (y in 0..maxY) {
                if (Coordinates2D(x, y) in finalPoints) {
//                    print('X')
                    sb.append('X')
                } else {
                    sb.append(' ')
                }
            }
            sb.append("\n")
        }
        println(sb.toString())
    }
}