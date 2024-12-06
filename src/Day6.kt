import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day06.txt")
    val inputMatrix: List<List<Char>> = input.map { it.toList() }

    println(part1(inputMatrix))
    println(part2(inputMatrix))
}

// Part 1
private fun part1(input: List<List<Char>>): Int {
    var (posi, posj) = findPosition(input)
    val map: MutableList<MutableList<Char>> = input.map { it.toMutableList() }.toMutableList()
    var direction = (-1 to 0)

    map[posi][posj] = 'X'
    while (posi+direction.first in map.indices && posj+direction.second in map[0].indices) {
        if(map[posi+direction.first][posj+direction.second] == '#') {
            direction = turn(direction)
        } else {
            posi += direction.first
            posj += direction.second
            map[posi][posj] = 'X'
        }
    }

    return map.flatten().count { it == 'X' }
}

private fun findPosition(input: List<List<Char>>): Pair<Int,Int> {
    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == '^') {
                return i to j
            }
        }
    }
    return -1 to -1
}

private fun turn(direction: Pair<Int, Int>): Pair<Int, Int> {
    return when (direction) {
        -1 to 0 -> 0 to 1 // Up -> Right
        0 to 1 -> 1 to 0 // Right -> Down
        1 to 0 -> 0 to -1 // Down -> Left
        0 to -1 -> -1 to 0 // Left -> Up
        else -> throw IllegalArgumentException("Invalid direction")
    }
}

// Part 2
private fun part2(input: List<List<Char>>): Int {
    var sol = 0
    for (i in input.indices) {
        for (j in input[0].indices) {
            if(input[i][j] == '^') continue // We can't put an obstacle in the starting position

            val tempMap = input.map { it.toMutableList() }.toMutableList()
            tempMap[i][j] = '#'
            sol += if (isLoop(tempMap)) 1 else 0
        }
    }
    return sol
}

private fun isLoop(map: List<List<Char>>): Boolean {
    var (posi, posj) = findPosition(map)
    var direction = (-1 to 0)
    val visited = mutableSetOf<Triple<Int, Int, Pair<Int, Int>>>() // i, j, direction
    visited.add(Triple(posi, posj, direction))

    while (posi+direction.first in map.indices && posj+direction.second in map[0].indices) {
        if(map[posi+direction.first][posj+direction.second] == '#') {
            direction = turn(direction)
        } else {
            posi += direction.first
            posj += direction.second
            if(Triple(posi, posj, direction) in visited) return true
            visited.add(Triple(posi, posj, direction))
        }
    }
    return false
}