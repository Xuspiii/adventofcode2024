import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

private val directions = listOf(
    Pair(-1, 0),
    Pair(0, 1),
    Pair(1, 0),
    Pair(0, -1)
)

fun main() {
    val input = readInput("inputs/day18.txt")
    val corruptedCoords = input.map { line -> line.split(",") }
        .map { it[1].toInt() to it[0].toInt() }
    println(part1(corruptedCoords))
    println(part2(corruptedCoords))
}

private fun part1(corruptedCoords: List<Pair<Int, Int>>): Int {
    val m = corruptedCoords.maxOfOrNull { it.first } ?: 0
    val n = corruptedCoords.maxOfOrNull { it.second } ?: 0

    val grid = Array(m+1) { Array(n+1) { 0 } }
    val firstCorruptedCoords = corruptedCoords.subList(0, 1024)

    findShortestPath(grid, 0 to 0, firstCorruptedCoords, 0)

    return grid[m][n]
}

private fun findShortestPath (grid: Array<Array<Int>>, position: Pair<Int, Int>, corruptedCoords: List<Pair<Int, Int>>, step: Int) {

    if (position == grid.size-1 to grid[0].size-1) {
        return
    }

    for (dir in directions) {
        val newPosition = Pair(position.first + dir.first, position.second + dir.second)

        if (newPosition.first !in grid.indices || newPosition.second !in grid[0].indices) continue
        if (newPosition in corruptedCoords) continue

        if(grid[newPosition.first][newPosition.second] == 0 || grid[newPosition.first][newPosition.second] > step + 1) {
            grid[newPosition.first][newPosition.second] = step + 1
            findShortestPath(grid, newPosition, corruptedCoords, step + 1)
        }
    }
}


private fun part2(corruptedCoords: List<Pair<Int, Int>>): Pair<Int, Int> {
    val m = corruptedCoords.maxOfOrNull { it.first } ?: 0
    val n = corruptedCoords.maxOfOrNull { it.second } ?: 0

    var corruptedCoordsCount = 1024
    while (true) {
        val newCorruptedCoords = corruptedCoords.subList(0, corruptedCoordsCount)
        val adjList = buildAdjList(m, n, newCorruptedCoords)

        val result = DFS(adjList, Pair(0, 0), Pair(m, n))

        if (result == -1) {
            return corruptedCoords[corruptedCoordsCount-1]
        }
        corruptedCoordsCount++
    }

    return Pair(-1, -1)
}

private fun buildAdjList(m: Int, n: Int, corruptedCoords: List<Pair<Int, Int>>): Map<Pair<Int,Int>, MutableList<Pair<Int,Int>>> {
    val adjList = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()

    for (x in 0..m) {
        for (y in 0..n) {
            val current = Pair(x, y)
            adjList[current] = mutableListOf()

            for (dir in directions) {
                val neighbor = Pair(x + dir.first, y + dir.second)

                if (neighbor.first in 0..m && neighbor.second in 0..n) {
                    if (!corruptedCoords.contains(neighbor) && !corruptedCoords.contains(current)) {
                        adjList[current]!!.add(neighbor)
                    }
                }
            }
        }
    }

    return adjList
}

private fun DFS(
    adjList: Map<Pair<Int, Int>, MutableList<Pair<Int, Int>>>,
    start: Pair<Int, Int>,
    target: Pair<Int, Int>
): Int {
    val stack = mutableListOf<Pair<Int, Int>>()
    val visited = mutableSetOf<Pair<Int, Int>>()
    stack.add(start)

    while (stack.isNotEmpty()) {
        val position = stack.removeAt(stack.size - 1)

        if (position == target) {
            return 1
        }
        if (position in visited) {
            continue
        }
        visited.add(position)

        for (neighbor in adjList[position]!!) {
            if (neighbor !in visited) {
                stack.add(neighbor)
            }
        }
    }

    return -1
}