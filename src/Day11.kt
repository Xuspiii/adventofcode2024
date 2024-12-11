import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day11.txt")[0]
    val inputArray = input.split(" ").map { it.toLong() }
    println(part1(inputArray))
    println(part2(inputArray))
}

private fun part1(input: List<Long>): Long {
    var sol = 0L
    for (stone in input) {
        sol += blink(stone, 0)
    }
    return sol
}

private fun blink(stone: Long, step: Int): Long {
    if (step == 25) return 1

    if(stone == 0L) {
        return blink(1, step+1)
    } else if (stone.toString().length % 2 == 0) {
        val mid = stone.toString().length / 2
        val left = stone.toString().substring(0, mid).toLong()
        val right = stone.toString().substring(mid).toLong()
        return blink(left, step+1) + blink(right, step+1)
    } else {
        return blink(stone*2024, step+1)
    }
}

private fun part2(input: List<Long>): Long {

    val mapValues = mutableMapOf<Long, Long>()
    for (stone in input) {
        mapValues[stone] = 1
    }

    for (i in 1..75) {
        val newMapValues = mutableMapOf<Long, Long>()
        for ((stone, count) in mapValues) {
            if (stone == 0L) {
                newMapValues[1] = newMapValues.getOrDefault(1, 0) + count
            } else if (stone.toString().length % 2 == 0) {
                val mid = stone.toString().length / 2
                val left = stone.toString().substring(0, mid).toLong()
                val right = stone.toString().substring(mid).toLong()
                newMapValues[left] = newMapValues.getOrDefault(left, 0) + count
                newMapValues[right] = newMapValues.getOrDefault(right, 0) + count
            } else {
                newMapValues[stone*2024] = newMapValues.getOrDefault(stone*2024, 0) + count
            }
        }
        mapValues.clear()
        mapValues.putAll(newMapValues)
    }

    return mapValues.values.sum()
}

// My first attempt:
// This leads us to a Stack Overflow
//
//private fun part2(input: List<Long>): Long {
//    var sol = 0L
//    for (stone in input) {
//        sol += blink2(stone, 0)
//    }
//    return sol
//}
//
//private fun blink2(stone: Long, step: Int): Long {
//    if (step == 75) return 1
//
//    if(stone == 0L) {
//        return blink2(1, step+1)
//    } else if (stone.toString().length % 2 == 0) {
//        val mid = stone.toString().length / 2
//        val left = stone.toString().substring(0, mid).toLong()
//        val right = stone.toString().substring(mid).toLong()
//        return blink2(left, step+1) + blink(right, step+1)
//    } else {
//        return blink2(stone*2024, step+1)
//    }
//}

// My second attempt:
// I've had this running for quite a while and it hasn't finished
//
//private fun part2(input: List<Long>): Long {
//    var sol = 0L
//
//    val stack = mutableListOf<Pair<Long, Int>>()
//    for (stone in input) {
//        stack.add(Pair(stone, 0))
//    }
//
//    while (stack.isNotEmpty()) {
//        val (stone, step) = stack.removeAt(stack.size-1)
//        if (step == 25) {
//            sol++
//            continue
//        }
//
//        if (stone == 0L) {
//            stack.add(Pair(1, step+1))
//        } else if (stone.toString().length % 2 == 0) {
//            val mid = stone.toString().length / 2
//            val left = stone.toString().substring(0, mid).toLong()
//            val right = stone.toString().substring(mid).toLong()
//            stack.add(Pair(left, step+1))
//            stack.add(Pair(right, step+1))
//        } else {
//            stack.add(Pair(stone*2024, step+1))
//        }
//    }
//
//    return sol
//}