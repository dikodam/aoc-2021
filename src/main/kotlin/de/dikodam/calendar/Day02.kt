package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.calendar.CommandType.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day02().executeTasks()
}

@ExperimentalTime
class Day02 : AbstractDay() {
    val input = readInputStrings()
    override fun task1(): String {
        return input.map { parseInstructionTask1(it) }
            .fold(Corrdinates2D(0, 0), Corrdinates2D::plus)
            .let { (horizontal, depth) -> "${horizontal * depth}" }
    }

    override fun task2(): String {
        return input.map { parseInstructionTask2(it) }
            .fold(SubmarineState(0, 0, 0)) { state, command -> state.process(command) }
            .run { "${horizontal * depth}" }
    }

    private fun parseInstructionTask1(instructionLine: String): Corrdinates2D {
        val (direction, countString) = instructionLine.split(Regex(" "))
        val count = countString.toInt()
        return when (direction) {
            "forward" -> Corrdinates2D(count, 0)
            "up" -> Corrdinates2D(0, -count)
            "down" -> Corrdinates2D(0, count)
            else -> throw IllegalArgumentException("wtf: $instructionLine")
        }
    }

    private fun parseInstructionTask2(line: String): MoveCommand {
        val (direction, countString) = line.split(Regex(" "))
        val count = countString.toInt()
        return when (direction) {
            "forward" -> MoveCommand(FORWARD, count)
            "up" -> MoveCommand(UP, count)
            "down" -> MoveCommand(DOWN, count)
            else -> throw IllegalArgumentException("wtf: $line")
        }
    }
}

private data class Corrdinates2D(val x: Int, val y: Int) {
    operator fun plus(other: Corrdinates2D) =
        Corrdinates2D(x + other.x, y + other.y)
}

private enum class CommandType {
    DOWN, UP, FORWARD
}

private data class MoveCommand(val commandType: CommandType, val amount: Int)

private data class SubmarineState(val horizontal: Int, val depth: Int, val aim: Int) {
    fun process(command: MoveCommand): SubmarineState {
        val (commandType, amount) = command
        return when (commandType) {
            DOWN -> this.copy(aim = aim + amount)
            UP -> this.copy(aim = aim - amount)
            FORWARD -> this.copy(horizontal = horizontal + amount, depth = depth + aim * amount)
        }
    }
}
