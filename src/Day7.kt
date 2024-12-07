import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day07.txt")
    println(part1(input))
    println(part2(input))
}

// Part 1
private fun part1(input: List<String>): Long {
    var sol = 0L;
    for (line in input) {
        val (expectedString, numbersString) = line.split(":")
        val expected = expectedString.toLong()
        val numbers = numbersString.trim().split(" ").map { it.toLong() }
        sol += if (isEquationPossible(expected, numbers)) expected else 0
    }
    return sol
}

private fun isEquationPossible(expected: Long, numbers: List<Long>): Boolean {
    if(numbers.size == 1) {
        return if(numbers[0] == expected) true else false
    }
    val sum = numbers[0] + numbers[1]
    val mult = numbers[0] * numbers[1]
    return isEquationPossible(expected, listOf(sum)+numbers.drop(2)) ||
            isEquationPossible(expected, listOf(mult)+numbers.drop(2))
}

// Part 2
private fun part2(input: List<String>): Long {
    var sol = 0L;
    for (line in input) {
        val (expectedString, numbersString) = line.split(":")
        val expected = expectedString.toLong()
        val numbers = numbersString.trim().split(" ").map { it.toLong() }
        sol += if (isEquationWithConcatPossible(expected, numbers)) expected else 0
    }
    return sol
}

private fun isEquationWithConcatPossible(expected: Long, numbers: List<Long>): Boolean {
    if(numbers.size == 1) {
        return if(numbers[0] == expected) true else false
    }
    val sum = numbers[0] + numbers[1]
    val mult = numbers[0] * numbers[1]
    val concat = (numbers[0].toString() + numbers[1].toString()).toLong()
    return isEquationWithConcatPossible(expected, listOf(sum)+numbers.drop(2)) ||
            isEquationWithConcatPossible(expected, listOf(mult)+numbers.drop(2)) ||
            isEquationWithConcatPossible(expected, listOf(concat)+numbers.drop(2))
}