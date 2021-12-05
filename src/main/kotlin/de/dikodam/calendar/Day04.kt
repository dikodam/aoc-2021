package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime


@ExperimentalTime
fun main() {
    Day04().executeTasks()
}

private const val lineBreak = "\r\n"

class Day04 : AbstractDay() {

    val rawInput = readInputRaw()

    override fun task1(): String {
        val (bingoNumers: List<Int>, bingoBoards: List<BingoBoard>) = parseInput(rawInput)
        return bingoBoards.map { it.processUntilWin(bingoNumers) }
            .minByOrNull { winningState -> winningState.index }
            ?.score.toString()
    }

    private fun parseInput(rawInput: String): Pair<List<Int>, List<BingoBoard>> {
        val splitInput = rawInput.split("$lineBreak$lineBreak")
        val bingoNumbers = splitInput[0].split(",")
            .map { it.toInt() }
        val bingoBoards = splitInput.drop(1)
            .map { BingoBoard.from(it) }
        return bingoNumbers to bingoBoards
    }

    override fun task2(): String {
        val (bingoNumers: List<Int>, bingoBoards: List<BingoBoard>) = parseInput(rawInput)
        return bingoBoards.map { it.processUntilWin(bingoNumers) }
            .maxByOrNull { winningState -> winningState.index }
            ?.score.toString()
    }
}

class BingoBoard(val bingoCells: List<BingoCell>) {

    class BingoCell(val number: Int, var marked: Boolean = false) {
        fun mark() {
            marked = true
        }
    }

    companion object {
        fun from(rawString: String): BingoBoard {
            val bingoCells = rawString.replace(lineBreak, " ")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .map { BingoCell(it) }
            return BingoBoard(bingoCells)
        }
    }

    val winningLines = listOf(
        listOf(0, 1, 2, 3, 4),
        listOf(5, 6, 7, 8, 9),
        listOf(10, 11, 12, 13, 14),
        listOf(15, 16, 17, 18, 19),
        listOf(20, 21, 22, 23, 24),
        listOf(0, 5, 10, 15, 20),
        listOf(1, 6, 11, 16, 21),
        listOf(2, 7, 12, 17, 22),
        listOf(3, 8, 13, 18, 23),
        listOf(4, 9, 14, 19, 24),
        listOf(0, 6, 12, 18, 24),
        listOf(4, 8, 12, 16, 20)
    )

    fun boardIsWinning(): Boolean {
        return winningLines
            .any { winningLine -> // board is winning if any winning line has all marked bingo cells
                winningLine.all { index -> bingoCells[index].marked }
            }
    }

    fun processUntilWin(numbers: List<Int>): WinningState {
        for (index in numbers.indices) {
            val number = numbers[index]
            processNumber(number)
            if (boardIsWinning()) {
                val score = number * bingoCells.filterNot { it.marked }.sumOf { it.number }
                return WinningState(index, score)
            }
        }
        error("something went wrong, processed all numbers but board didnt win")
    }

    fun processNumber(i: Int) {
        bingoCells.filter { it.number == i }.forEach { it.mark() }
    }
}

data class WinningState(val index: Int, val score: Int)