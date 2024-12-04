import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

private val diagonalDirections = listOf(
    1 to 1, 1 to -1, -1 to 1, -1 to -1
)
private val allDirections = listOf(
    1 to 0, 1 to 1, 0 to 1, -1 to 1,
    -1 to 0, -1 to -1, 0 to -1, 1 to -1
)

fun main() {
    val input = readInput("inputs/day04.txt")
    val inputArray: List<List<Char>> = input.map { it.toList() }

    println(part1(inputArray))
    println(part2(inputArray))
}

// Part 1
private fun part1(input: List<List<Char>>): Int {
    var sol = 0
    for(i in input.indices) {
        for(j in input[0].indices) {
            if(input[i][j] == 'X') {
                sol += findXmas(input, i, j)
            }
        }
    }
    return sol
}

private fun findXmas(input: List<List<Char>>, i: Int, j: Int): Int {
    var xmasFound = 0
    for (direction in allDirections) {
        val (dx, dy) = direction
        if(i+3*dx in input.indices && j+3*dy in input.indices) { // Check out of bounds
            if( input[i+dx][j+dy] == 'M' &&
                input[i+2*dx][j+2*dy] == 'A' &&
                input[i+3*dx][j+3*dy] == 'S'
                ) {
                xmasFound++
            }
        }
    }
    return xmasFound
}

// Part 2
private fun part2(input: List<List<Char>>): Int {
    var sol = 0

    for(i in input.indices) {
        for(j in input[0].indices) {
            if(input[i][j] == 'M') {
                sol += findMas(input, i, j)
            }
        }
    }
    return sol/2 // As each "MAS" has two 'M' it's counted twice
}

private fun findMas(input: List<List<Char>>, i: Int, j: Int): Int {
    var masFound = 0
    for (direction in diagonalDirections) {
        val (dx, dy) = direction
        if(i+2*dx in input.indices && j+2*dy in input.indices) { // Check out of bounds
            if ((input[i+dx][j+dy] == 'A' && input[i+2*dx][j+2*dy] == 'S')) { // First diagonal
                if ((input[i][j+2*dy] == 'M' && input[i+2*dx][j] == 'S') || // Second diagonal (both posibilities)
                    (input[i][j+2*dy] == 'S' && input[i+2*dx][j] == 'M')) {
                    masFound++
                }
            }
        }
    }
    return masFound
}