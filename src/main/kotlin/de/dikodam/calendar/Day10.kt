package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day10().executeTasks()
}

class Day10 : AbstractDay() {

    val input = readInputLines()
//    val input = """[({(<(())[]>[[{[]{<()<>>
//[(()[<>])]({[<{<<[]>>(
//{([(<{}[<>[]}>{[]{[(<()>
//(((({<>}<{<{<>}{[]{[]{}
//[[<[([]))<([[{}[[()]]]
//[{[{({}]{}}([{[{{{}}([]
//{<[[]]>}<{[{[{[]{()[[[]
//[<(<(<(<{}))><([]([]()
//<{([([[(<>()){}]>(<<{{
//<{([{{}}[<[[[<>{}]]]>[]]""".trim().lines()

    override fun task1(): String {

        fun Char.toPoints() =
            when (this) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> error("unknown char $this")
            }

        val result = input
            .map { it.diagnose() }
            .filterIsInstance<InvalidLine>()
            .map { it.firstInvalidChar }
            .map { it.toPoints() }
            .sumOf { it }
        return "$result"
    }

    override fun task2(): String {
        val sortedScores = input
            .map { it.diagnose() }
            .filterIsInstance<IncompleteLine>()
            .map { calculateAutocompletePoints(it.remainingOpeningTokens) }
            .sorted()

        return sortedScores[sortedScores.size / 2].toString()
    }

}

fun calculateAutocompletePoints(queue: Deque<Char>): Long {
    var sum = 0L
    while (queue.isNotEmpty()) {
        sum *= 5
        val next = queue.removeFirst() // TODO why not removeLast?
        sum += when (next) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> error("unknown char $next")
        }
    }
    return sum
}

fun Char.isOpening() = this in setOf('(', '{', '[', '<')
fun Char.matches(opening: Char) =
    when (this) {
        ')' -> opening == '('
        ']' -> opening == '['
        '}' -> opening == '{'
        '>' -> opening == '<'
        else -> error("unknown char $this")
    }

fun Char.isClosing() = this in setOf(')', '}', ']', '>')

fun String.diagnose(): DiagnosticReport {
    val stack: Deque<Char> = ArrayDeque()
    for (token in this) {
        if (token.isOpening()) {
            stack.addFirst(token)
            continue
        }
        val opening: Char = stack.removeFirst()
        if (!token.matches(opening)) {
            return InvalidLine(token)
        }
    }
    return if (stack.isNotEmpty()) {
        IncompleteLine(stack)
    } else {
        ValidLine
    }
}

sealed class DiagnosticReport
object ValidLine : DiagnosticReport()
data class IncompleteLine(val remainingOpeningTokens: Deque<Char>) : DiagnosticReport()
data class InvalidLine(val firstInvalidChar: Char) : DiagnosticReport()
