package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import de.dikodam.hexToBinString
import de.dikodam.splitAfter
import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day16().executeTasks()
}

class Day16 : AbstractDay() {


    val input = readInputRaw()
//    val input = "D2FE28"
//    val input = "38006F45291200"
//    val input = "EE00D40C823060"
        .hexToBinString()

    override fun task1(): String {

        val packets = parsePackets(input)

        val result = packets
            .map(Packet::sumOfVersions)
            .sum()

        return result.toString()
    }

    override fun task2(): String {
        val packets = parsePackets(input)

        val result = packets
            .map(Packet::evaluate)
            .sum()

        return result.toString()
    }

    fun parsePackets(input: String): List<Packet> {
        var remainingInput = input
        val packets = mutableListOf<Packet>()
        while (remainingInput.length > 7) { // in order to parse anything properly, we definitely need more than version(3) + type(3) + then some bits = 7
            val (packet, rest) = parseNextPacket(remainingInput)
            packets += packet
            remainingInput = rest
        }
        return packets
    }

}

fun parseNextPacket(input: String): Pair<Packet, String> {
    val version = input.take(3).toInt(2)
    val typeId = input.drop(3).take(3).toInt(2)
    val payload = input.drop(6)

    val (packet: Packet, remainingPayload) = if (typeId == 4) {
        parseLiteralValue(version, payload)
    } else {
        parseOperator(version, typeId, payload)
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

    val number = binaryNumber.toLong(2)
    return LiteralValue(version, number) to remainingString
}

fun parseOperator(version: Int, typeId: Int, payload: String): Pair<Operator, String> {
    val (readByModeChar, remainingPayload) = payload.splitAfter(1)
    val readByMode = ReadByMode.fromChar(readByModeChar)
    return when (readByMode) {
        ReadByMode.BITLENGTH -> parseOperatorByBitLength(version, typeId, remainingPayload)
        ReadByMode.PACKETCOUNT -> parseOperatorByPacketCount(version, typeId, remainingPayload)
    }
}

fun parseOperatorByBitLength(version: Int, typeId: Int, payload: String): Pair<Operator, String> {
    val bitsToRead = payload.take(15).toInt(2)
    var (payloadToRead, remainingPayload) = payload.drop(15).splitAfter(bitsToRead)
    val packets = mutableListOf<Packet>()
    while (payloadToRead.isNotEmpty()) {
        val (packet, remaining) = parseNextPacket(payloadToRead)
        packets += packet
        payloadToRead = remaining
    }
    return Operator(version, typeId, ReadByMode.BITLENGTH, packets) to remainingPayload
}

fun parseOperatorByPacketCount(version: Int, typeId: Int, payload: String): Pair<Operator, String> {
    val packetsToRead = payload.take(11).toInt(2)
    var remaining = payload.drop(11)
    val packets = mutableListOf<Packet>()
    var packetsRead = 0
    while (packetsRead < packetsToRead) {
        val (packet, rest) = parseNextPacket(remaining)
        packets += packet
        packetsRead++
        remaining = rest
    }
    return Operator(version, typeId, ReadByMode.PACKETCOUNT, packets) to remaining
}

sealed class Packet {
    abstract fun sumOfVersions(): Long
    abstract fun evaluate(): Long
}

data class LiteralValue(val version: Int, val number: Long) : Packet() {
    override fun sumOfVersions(): Long =
        version.toLong()

    override fun evaluate(): Long = number
}

data class Operator(val version: Int, val typeId: Int, val readBy: ReadByMode, val packets: List<Packet>) : Packet() {
    override fun sumOfVersions(): Long {
        return version.toLong() + packets.sumOf { it.sumOfVersions() }
    }

    override fun evaluate(): Long =
        when (typeId) {
            0 -> packets.sumOf { it.evaluate() }
            1 -> packets.map { it.evaluate() }.reduce(Long::times)
            2 -> packets.map { it.evaluate() }.reduce(::min)
            3 -> packets.map { it.evaluate() }.reduce(::max)
            5 -> if (packets[0].evaluate() > packets[1].evaluate()) 1 else 0
            6 -> if (packets[0].evaluate() < packets[1].evaluate()) 1 else 0
            7 -> if (packets[0].evaluate() == packets[1].evaluate()) 1 else 0
            else -> error("this should not happen :clown:")
        }
}

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
