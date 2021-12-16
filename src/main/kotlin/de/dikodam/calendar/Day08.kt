package de.dikodam.calendar

import de.dikodam.AbstractDay
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
//    Day08().executeTasks()

    println(('c' in "char"))


}

class Day08 : AbstractDay() {
    val input = readInputLines()

//    val input = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
//edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
//fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
//fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
//aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
//fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
//dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
//bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
//egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
//gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".lines()

    override fun task1(): String {
        val uniqueSizes = listOf(2, 4, 3, 7)
        val sumOfEasyDigitsInOutput = input.map { it.split(" | ")[1] }
            .map {
                it.split(" ")
                    .count { digit -> digit.length in uniqueSizes }
            }
            .sumOf { it }
        return "$sumOfEasyDigitsInOutput"
    }

    override fun task2(): String {
        return input.map { it.decode() }
            .sumOf { it }
            .toString()
    }
}

// digit : signalCount
// 0: 6
// 1: 2
// 2: 5
// 3: 5
// 4: 4
// 5: 5
// 6: 6
// 7: 3
// 8: 7
// 9: 6

//      0:      1:      2:      3:      4:
//     aaaa    ....    aaaa    aaaa    ....                 digitAppearances
//    b    c  .    c  .    c  .    c  b    c                a: 8
//    b    c  .    c  .    c  .    c  b    c                b: 6
//     ....    ....    dddd    dddd    dddd                 c: 8
//    e    f  .    f  e    .  .    f  .    f                d: 7
//    e    f  .    f  e    .  .    f  .    f                e: 4
//     gggg    ....    gggg    gggg    ....                 f: 9
//                                                          g: 7
//      5:      6:      7:      8:      9:
//     aaaa    aaaa    aaaa    aaaa    aaaa             signalCount : digits
//    b    .  b    .  .    c  b    c  b    c            2: (1)
//    b    .  b    .  .    c  b    c  b    c            3: (7)
//     dddd    dddd    ....    dddd    dddd             4: (4)
//    .    f  e    f  .    f  e    f  .    f            5: (2), (3), (5)
//    .    f  e    f  .    f  e    f  .    f            6: 0, (6), 9
//     gggg    gggg    ....    gggg    gggg             7: (8)

// c, f, a
// 1, 2, 3, 4, 5, 6, 7

// 1. find 1 (length 2) and is c + f
// f appears 9/19 times -> c DONE, f DONE
// 2. 7 has length 3 and is c + f + a
// c only doesn't appear in 5 (size 5) and 6 (size 6) -> 5 identified, 6 identified
// only 2 doesn't have f -> 2 identified
// at this point, the remaining 5-sized digit is 3

// remaining pattern are ( both 6 length) 0 and 9
// 0 has unique e, 9 has unique d (intersection: a,b,c,f,g)
// e is identifiable, therefore 0 is, the remaining pattern is 9

fun String.decode(): Int {
    val (patterns, solutionDigitpatterns) = this.split(" | ").map { it.split(" ") }
    val patternsByLength: Map<Int, List<String>> = patterns.groupBy { it.length }
    val signalCounts: Map<Char, Int> = countSignalsInPatterns(patterns)

    val patternToDigit = mutableMapOf<String, Int>()

    val pattern1 = patternsByLength[2]!![0]
    val pattern7 = patternsByLength[3]!![0]
    val pattern4 = patternsByLength[4]!![0]
    val pattern8 = patternsByLength[7]!![0]
    patternToDigit[pattern1] = 1
    patternToDigit[pattern4] = 4
    patternToDigit[pattern7] = 7
    patternToDigit[pattern8] = 8

    val (signalC, signalF) = identifySignalCandF(pattern1, signalCounts)
    val signalA = identifySignalA(pattern7, signalC, signalF)

    // c only doesn't appear in 5 (size 5) and 6 (size 6)
    val pattern5: String = patternsByLength[5]!!.first { !it.contains(signalC) }
    val pattern6: String = patternsByLength[6]!!.first { !it.contains(signalC) }
    patternToDigit[pattern5] = 5
    patternToDigit[pattern6] = 6

    // only 2 doesn't have f
    val pattern2 = patterns.filterNot { it.contains(signalF) }.first()
    patternToDigit[pattern2] = 2

    // at this point, the remaining 5-sized digit is 3
    val pattern3 = patterns.filter { it.length == 5 }.first { it != pattern2 && it != pattern5 }
    patternToDigit[pattern3] = 3

    // remaining patterns are ( both 6 length) 0 and 9
    // 0 has unique e, 9 has unique d (intersection: a,b,c,f,g)
    // e is identifiable (appears in 4 patterns), therefore 0 is, the remaining pattern is 9

    val patterns0And9 = patterns.filter { it.length == 6 }.filter { it != pattern6 }
    val (signalE, _) = signalCounts.asSequence().first { (_, count) -> count == 4 }
    val pattern0 = patterns0And9.first { it.contains(signalE) }
    val pattern9 = patterns0And9.first { !it.contains(signalE) }
    patternToDigit[pattern0] = 0
    patternToDigit[pattern9] = 9

    return solutionDigitpatterns
        .map { pattern -> matchDigit(patternToDigit, pattern) }
        .joinToString(separator = "")
        .toInt()
}

fun matchDigit(patternToDigit: Map<String, Int>, pattern: String): String {
// TODO fix this
    val (_, digit) = patternToDigit.asSequence()
        .filter { (key, _) -> key.all { kChar -> pattern.contains(kChar) } && pattern.all { pChar -> key.contains(pChar) } }
        .first()
    return digit.toString()
}

fun identifySignalA(pattern7: String, c: Char, f: Char): Char {
    return pattern7.toList().first { it != c && it != f }
}

fun identifySignalCandF(pattern1: String, signalCounts: Map<Char, Int>): Pair<Char, Char> {
    // c has signalCount of 8
    // f has signalCount of 9
    val (c, _) = signalCounts.asSequence()
        .first { (char, count) -> pattern1.contains(char) && count == 8 }
    val (f, _) = signalCounts.asSequence()
        .first { (char, count) -> pattern1.contains(char) && count == 9 }
    return c to f
}

fun countSignalsInPatterns(patterns: List<String>): Map<Char, Int> {
    val distinctChars = patterns.flatMap { it.toList() }.distinct()
    return distinctChars.associateWith { char -> patterns.count { pattern -> char in pattern } }
}