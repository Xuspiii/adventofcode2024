import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day08.txt")
    val inputMatrix: List<List<Char>> = input.map { it.toList() }

    println(part1(inputMatrix))
    println(part2(inputMatrix))
}

// Part 1
private fun part1(input: List<List<Char>>): Int {
    val signals = mutableSetOf<Pair<Int,Int>>() // Positions on the map where there is a signal
    val antennaFrequencies = input.flatten().filter { it != '.' }.toSet()
    antennaFrequencies.forEach { frequency ->
        addSignal(input, signals, frequency)
    }
    return signals.size
}

private fun addSignal(input: List<List<Char>>, signals: MutableSet<Pair<Int,Int>>, frequency: Char) {
    val frequencyPositions = mutableListOf<Pair<Int,Int>>()
    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == frequency) {
                frequencyPositions.add(i to j)
            }
        }
    }

    for (i in 0..<frequencyPositions.size-1) {
        for (j in i+1..<frequencyPositions.size) {
            val (x1, y1) = frequencyPositions[i]
            val (x2, y2) = frequencyPositions[j]
            val (dx, dy) = (x2-x1) to (y2-y1)

            if(x1-dx in input.indices && y1-dy in input[0].indices) { // Check if the position is inside the map
                signals.add(x1-dx to y1-dy)
            }

            if(x2+dx in input.indices && y2+dy in input[0].indices) {
                signals.add(x2+dx to y2+dy)
            }
        }
    }
}

// Part 2
private fun part2(input: List<List<Char>>): Int {
    val signals = mutableSetOf<Pair<Int,Int>>() // Positions on the map where there is a signal
    val antennaFrequencies = input.flatten().filter { it != '.' }.toSet()
    antennaFrequencies.forEach { frequency ->
        addSignal(input, signals, frequency)
    }
    return signals.size
}

private fun addSignalWithAntinodes(input: List<List<Char>>, signals: MutableSet<Pair<Int,Int>>, frequency: Char) {
    val frequencyPositions = mutableListOf<Pair<Int,Int>>()
    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == frequency) {
                frequencyPositions.add(i to j)
            }
        }
    }

    for (i in 0..<frequencyPositions.size-1) {
        for (j in i+1..<frequencyPositions.size) {
            val (x1, y1) = frequencyPositions[i]
            val (x2, y2) = frequencyPositions[j]
            val (dx, dy) = (x2-x1) to (y2-y1)

            var m = 0
            while (x1 - m*dx in input.indices && y1 - m*dy in input[0].indices) {
                signals.add(x1-m*dx to y1-m*dy)
                m++
            }

            var n=0
            while (x2 + n*dx in input.indices && y2 + n*dy in input[0].indices) {
                signals.add(x2+n*dx to y2+n*dy)
                n++
            }
        }
    }
}