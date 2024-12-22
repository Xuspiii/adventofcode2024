import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day22.txt").map { it.toLong() }
    println(part1(input))
}

private fun part1(input: List<Long>): Long {
    var sol = 0L

    for (number in input) {
        var num = number
        repeat(2000) {
            num = (num * 64).xor(num) % 16777216
            num = (num / 32).xor(num) % 16777216
            num = (num * 2048).xor(num) % 16777216
        }
        println(num)
        sol += num
    }

    return sol
}