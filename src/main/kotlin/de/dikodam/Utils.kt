package de.dikodam

data class Coordinates2D(val x: Int, val y: Int) {
    operator fun plus(other: Coordinates2D) =
        Coordinates2D(x + other.x, y + other.y)
}