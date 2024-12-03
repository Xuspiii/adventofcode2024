import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day01.txt")

    val arr1 = mutableListOf<Int>()
    val arr2 = mutableListOf<Int>()

    for(line in input) {
        arr1.add(line.substringBefore(" ").toInt())
        arr2.add(line.substringAfterLast(" ").toInt())
    }

    println(part1(arr1, arr2))
    println(part2(arr1, arr2))
}

private fun part1(arr1: List<Int>, arr2: List<Int>): Int {
    val sortedArr1 = arr1.sorted()
    val sortedArr2 = arr2.sorted()
    var sol = 0
    for(i in 0..sortedArr1.lastIndex) {
        sol += abs(sortedArr1[i] - sortedArr2[i])
    }
    return sol
}

private fun part2(arr1: MutableList<Int>, arr2: MutableList<Int>): Int {
    val frequencyMap = arr2.groupingBy { it }.eachCount()
    var sol = 0
    arr1.forEach { sol += it * frequencyMap.getOrDefault(it, 0) }
    return (sol)
}