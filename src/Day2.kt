import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day02.txt")

    println(part1(input))
    println(part2(input))
}

// Part 1
private fun part1(input: List<String>): Int {
    var sol = 0
    for(line in input) {
        val numbers = line.split(" ").map { it.toInt() }
        if(isSafe(numbers)) sol++
    }
    return sol
}

private fun isSafe(numbers: List<Int>): Boolean {
    // Decreasing
    if(numbers[0] > numbers[1]) {
        for (i in 0..<numbers.lastIndex) {
            if (numbers[i] <= numbers[i + 1] || numbers[i] - numbers[i + 1] > 3) return false
        }
    // Increasing
    } else {
        for (i in 0..<numbers.lastIndex) {
            if (numbers[i+1] <= numbers[i] || numbers[i+1] - numbers[i] > 3) return false
        }
    }
    return true
}


// Part 2
private fun part2(input: List<String>): Int {
    var sol = 0
    for(line in input) {
        val numbers = line.split(" ").map { it.toInt() }
        if(isSafePart2(numbers)) sol++
    }
    return sol
}

private fun isSafePart2(numbers: List<Int>): Boolean {
    if(isSafe(numbers)) return true
    for(i in 0..numbers.lastIndex) {
        val tempNumbers = numbers.toMutableList()
        tempNumbers.removeAt(i)
        if(isSafe(tempNumbers)) return true
    }
    return false
}