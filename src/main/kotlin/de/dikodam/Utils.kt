package de.dikodam

import de.dikodam.calendar.parseLiteralValue

data class Coordinates2D(val x: Int, val y: Int) {
    operator fun plus(other: Coordinates2D) =
        Coordinates2D(x + other.x, y + other.y)
}

fun String.hexToBinString(): String =
    this.map { char -> char.digitToInt(16) }
        .map { Integer.toBinaryString(it) }
        .map { it.padStart(4, '0') }
        .joinToString("")

fun String.splitAfter(count: Int): Pair<String, String> =
    take(count) to drop(count)

fun <T> Iterable<T>.splitAfter(count: Int): Pair<List<T>, List<T>> =
    take(count) to drop(count)

fun main() {
    println(parseLiteralValue(6, "101111111000101"))
}