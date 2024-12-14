import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day14.txt")
    val robots = mutableListOf<Robot>()

    for (line in input) {
        val pos = line.split(" ")[0]
        val dir = line.split(" ")[1]
        val position = pos.substringAfter("=").split(",").map { it.toInt() }
        val direction = dir.substringAfter("=").split(",").map { it.toInt() }
        val robot = Robot(position[0] to position[1], direction[0] to direction[1])
        robots.add(robot)
    }

    println(part1(robots))
    println(part2(robots))
}

// Part 1
private fun part1(robots: List<Robot>): Long {
    val quadrantValues = mutableListOf(0, 0, 0, 0, 0)
    for (robot in robots) {
        quadrantValues[getQuadrant(robot, 103, 101)]++
    }
    println(quadrantValues)
    var sol = 1L
    for (i in 0..3) {
        sol *= quadrantValues[i]
    }
    return sol
}

private fun getQuadrant(robot: Robot, tall: Int, wide: Int): Int {
    val (x, y) = robot.initialPosition
    val (dx, dy) = robot.direction

    val finalPosition = ((x + 100 * dx) % wide + wide) % wide to ((y + 100 * dy) % tall + tall) % tall

    return when {
        finalPosition.first > wide/2 && finalPosition.second < tall/2 -> 0
        finalPosition.first < wide/2 && finalPosition.second < tall/2 -> 1
        finalPosition.first < wide/2 && finalPosition.second > tall/2 -> 2
        finalPosition.first > wide/2 && finalPosition.second > tall/2 -> 3
        else -> 4
    }
}

private class Robot(val initialPosition: Pair<Int, Int>, val direction: Pair<Int, Int>) {

}

// Part 2
private fun part2(robots: List<Robot>): Int {
    printEverySecondUntil(robots, 10000)
    return 0
}

private fun printEverySecondUntil(robots: List<Robot>, seconds: Int, tall: Int = 103, wide: Int = 101) {

    val outputFile = File("output.txt")
    outputFile.writeText("")

    for (i in 0..seconds) {
        val positions = mutableListOf<Pair<Int, Int>>()
        for (robot in robots) {
            val (x, y) = robot.initialPosition
            val (dx, dy) = robot.direction
            val finalPosition = ((x + i * dx) % wide + wide) % wide to ((y + i * dy) % tall + tall) % tall
            positions.add(finalPosition)
        }
        val map = Array(tall) { CharArray(wide) { '.' } }
        for (position in positions) {
            map[position.second][position.first] = '#'
        }

        outputFile.appendText("\nSecond $i\n\n")
        for (row in map) {
            outputFile.appendText(row.joinToString("") + "\n")
        }
    }
}