package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day20().executeTasks()
}

val test1 =
    """..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###""".lines()

class Day20 : AbstractDay() {

    //val rawInputLines = readInputLines()
    val rawInputLines = test1
    val enhancementAlgo = rawInputLines[0]
    val inputPicture = rawInputLines.drop(2)

    override fun task1(): String {

        var picture: Set<Coordinates2D> =
            inputPicture.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Coordinates2D(x, y) to char } }
                .filter { (_, char) -> char == '#' }
                .map { (coords) -> coords }
                .toSet()

        visualize(picture)

        repeat(2) {
            picture = extendImage(picture)
            visualize(picture)
        }

        return picture.size.toString()
    }

    fun visualize(picture: Set<Coordinates2D>) {
        val minX = picture.minOf { it.x }
        val maxX = picture.maxOf { it.x }
        val minY = picture.minOf { it.y }
        val maxY = picture.maxOf { it.y }
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val char = if (Coordinates2D(x, y) in picture) '#' else '.'
                print(char)
            }
            println()
        }
        println()
    }

    fun extendImage(picture: Set<Coordinates2D>): Set<Coordinates2D> {
        return findAllEligibleCoords(picture)
            .map { newPixel -> newPixel to newPixel.computeEnhancementIndex(picture) }
            .filter { (_, index) -> lookupEnhancement(index) }
            .map { (newPixel, _) -> newPixel }
            .toSet()
    }


    fun findAllEligibleCoords(picture: Set<Coordinates2D>): Set<Coordinates2D> {
        val minX = picture.minOf { it.x } - 1
        val maxX = picture.maxOf { it.x } + 1
        val minY = picture.minOf { it.y } - 1
        val maxY = picture.maxOf { it.y } + 1
        val xs = generateSequence(minX, Int::inc)
            .takeWhile { x -> x <= maxX }
        val ys = generateSequence(minY, Int::inc)
            .takeWhile { y -> y <= maxY }
        return xs.flatMap { x -> ys.map { y -> Coordinates2D(x, y) } }
            .toSet()
    }

    fun Coordinates2D.computeEnhancementIndex(picture: Set<Coordinates2D>): Int {
        val xs = (x - 1)..(x + 1)
        val ys = (y - 1)..(y + 1)
        var index = ""
        val points = xs.flatMap { newX -> ys.map { newY -> newX to newY } }
            .map { (newX, newY) -> Coordinates2D(newX, newY) }
        for (point in points) {
            val activation = if (point in picture) "1" else "0"
            index = activation + index
        }
        return index
            .toInt(2)
    }

    fun lookupEnhancement(index: Int): Boolean =
        enhancementAlgo[index] == '#'

    override fun task2(): String {
        return ""
    }
}