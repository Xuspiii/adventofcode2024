import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val patterns = readInput("inputs/day19patterns.txt")[0].split(", ")
    val towels = readInput("inputs/day19designs.txt")

    println(part1(patterns, towels))
    println(part2(patterns, towels))
}

private fun part1(patterns: List<String>, towels: List<String>): Int {

    var count = 0

    for (towel in towels) {
        if (canBeMade(patterns, towel)) {
            count++
        }
    }

    return count
}

private fun canBeMade(patterns: List<String>, towel: String, visited: MutableSet<String> = mutableSetOf()): Boolean {

    if (towel in visited) {
        return false
    }
    if (towel.isEmpty()) {
        return true
    }
    visited.add(towel)

    for (pattern in patterns) {
        if (towel.startsWith(pattern)) {
            val newTowel = towel.removePrefix(pattern)
            if (newTowel in visited) {
                continue
            }
            if (canBeMade(patterns, newTowel, visited)) return true
        }
    }

    return false
}

private fun part2(patterns: List<String>, towels: List<String>): Long {

    var count = 0L

    for (towel in towels) {
        count += countWaysToMakeTowel(patterns, towel)
    }

    return count
}

private fun countWaysToMakeTowel(patterns: List<String>, towel: String): Long {

    val memo = mutableMapOf("" to 1L)
    for (i in 1..towel.length) {
        memo[towel.substring(0, i)] = 0
    }

    for (i in 1..towel.length) {
        for (pattern in patterns) {
            if (towel.substring(0, i).endsWith(pattern)) {
                memo[towel.substring(0, i)] = memo[towel.substring(0, i)]!! + memo[towel.substring(0, i).removeSuffix(pattern)]!!
            }
        }
    }

    return memo[towel]!!
}