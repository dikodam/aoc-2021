@file:OptIn(ExperimentalTime::class)

package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import de.dikodam.hexToBinString
import de.dikodam.splitAfter
import kotlin.time.ExperimentalTime

fun main() {
    Day16().executeTasks()
}

class Day16 : AbstractDay() {

    val input = readInputRaw().hexToBinString()

    override fun task1(): String {

        return input
    }

    override fun task2(): String {
        return ""
    }
}

fun parseNextPacket(input: String): Pair<Packet, String> {
    val version = input.take(3).toInt(2)
    val typeId = input.drop(3).take(3).toInt(2)
    val payload = input.drop(6)

    val (packet: Packet, remainingPayload) = if (typeId == 4) {
        parseLiteralValue(version, payload)
    } else {
        parseOperator(version, payload)
    }

    return packet to remainingPayload
}

fun parseLiteralValue(version: Int, payload: String): Pair<LiteralValue, String> {
    var remainingString = payload
    var morePartsToParse = true
    var binaryNumber = ""
    while (morePartsToParse) {
        val numberPart = remainingString.take(5)
        val (keepGoing, numberDigits) = numberPart.splitAfter(1)
        binaryNumber += numberDigits
        morePartsToParse = keepGoing == "1"
        remainingString = remainingString.drop(5)
    }

    val number = binaryNumber.toInt(2)
    return LiteralValue(version, number) to remainingString
}

fun parseOperator(version: Int, payload: String): Pair<Operator, String> {
    val (readByModeChar, remainingPayload) = payload.splitAfter(1)
    val readByMode = ReadByMode.fromChar(readByModeChar)
    return  when(readByMode) {
        ReadByMode.BITLENGTH -> parseOperatorByBitLength(version, remainingPayload)
        ReadByMode.PACKETCOUNT -> parseOperatorByPacketSize(version, remainingPayload)
    }
}

fun parseOperatorByBitLength(version:Int, payload:String): Pair<Operator, String> {
    val bitCountToRead = payload.take(15).toInt(2)
    var remainingPayload = payload.drop(15)


    return Operator(version, ReadByMode.BITLENGTH, emptyList()) to remaining
}

fun parseOperatorByPacketSize(version:Int, payload:String): Pair<Operator, String> {
    val packetsToRead = payload.take(11).toInt(2)
    payload.drop(11)
    return Operator(version, ReadByMode.BITLENGTH, emptyList()) to remaining
}


sealed class Packet
data class LiteralValue(val version: Int, val number: Int) : Packet()
data class Operator(val version: Int, val readBy: ReadByMode, val packets: List<Packet>) : Packet()

enum class ReadByMode {
    BITLENGTH, PACKETCOUNT;

    companion object {
        fun fromChar(readBy: String) =
            when (readBy) {
                "0" -> BITLENGTH
                else -> PACKETCOUNT
            }
    }
}