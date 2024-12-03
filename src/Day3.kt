import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

private const val mulRegex = """mul\((?<first>\d{1,3}),(?<second>\d{1,3})\)"""
private const val doRegex = """do\(\)"""
private const val dontRegex = """don't\(\)"""
private const val mulOrDoOrDontRegex = """$mulRegex|$doRegex|$dontRegex"""

fun main() {
    val input = readInput("inputs/day03.txt").joinToString("")

    println(part1(input))
    println(part2(input))
}

private fun part1(input: String): Int {
    var sum = 0
    mulRegex.toRegex().findAll(input).forEach { match ->
        val first = match.groups["first"]?.value?.toInt() ?: 0
        val second = match.groups["second"]?.value?.toInt() ?: 0
        sum += first * second
    }
    return sum
}

private fun part2(input: String): Int {
    var sum = 0
    var enabled = true

    mulOrDoOrDontRegex.toRegex().findAll(input).forEach { match ->
        when(match.value) {
            "do()" -> enabled = true
            "don't()" -> enabled = false
            else -> {
                if(enabled) {
                    val first = match.groups["first"]?.value?.toInt() ?: 0
                    val second = match.groups["second"]?.value?.toInt() ?: 0
                    sum += first * second
                }
            }
        }
    }
    return sum
}
