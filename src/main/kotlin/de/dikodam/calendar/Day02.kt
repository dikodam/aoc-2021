package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.calendar.CommandType.*
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day02().executeTasks()
}

class Day02 : AbstractDay() {
    val input = readInputStrings()
    override fun task1(): String {
        return input.map { parseInstructionTask1(it) }
            .fold(Coordinates2D(0, 0), Coordinates2D::plus)
            .let { (horizontal, depth) -> "${horizontal * depth}" }
    }

    override fun task2(): String {
        return input.map { parseInstructionTask2(it) }
            .fold(SubmarineState(0, 0, 0), SubmarineState::process)
            .run { "${horizontal * depth}" }
    }

    private fun parseInstructionTask1(instructionLine: String): Coordinates2D {
        val (direction, countString) = instructionLine.split(Regex(" "))
        val count = countString.toInt()
        return when (direction) {
            "forward" -> Coordinates2D(count, 0)
            "up" -> Coordinates2D(0, -count)
            "down" -> Coordinates2D(0, count)
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
