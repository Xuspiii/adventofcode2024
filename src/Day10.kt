import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

private val directions: List<Pair<Int, Int>> = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

fun main() {
    val input = readInput("inputs/day10.txt")
    val inputMatrix: List<List<Int>> = input.map { line -> line.map { it.digitToInt()} }

    println(part1(inputMatrix))
    println(part2(inputMatrix))
}

// Part 1
private fun part1(map: List<List<Int>>): Int {
    var sol = 0

    for (i in map.indices) {
        for (j in map[i].indices) {
            if(map[i][j] == 0) {
                sol += findPeaks(map, i, j)
            }
        }
    }

    return sol
}

private fun findPeaks(map: List<List<Int>>, i: Int, j: Int): Int {

    val peaks = mutableSetOf<Pair<Int, Int>>()
    ascendingDFS(map, i, j, peaks)

    return peaks.size
}

private fun ascendingDFS(map: List<List<Int>>, x: Int, y: Int, peaks: MutableSet<Pair<Int, Int>>) {

    if(map[x][y] == 9) {
        peaks.add(Pair(x, y))
        return
    }

    for(dir in directions) {
        val (dx, dy) = dir
        if(x+dx in map.indices && y+dy in map[0].indices) {
            if(map[x+dx][y+dy] == map[x][y] + 1) {
                ascendingDFS(map, x+dx, y+dy, peaks)
            }
        }
    }
}

// Part 2
private fun part2(map: List<List<Int>>): Int {
    val ratingMap = mutableMapOf<Pair<Int,Int>, Int>()

    for (i in map.indices) {
        for (j in map[i].indices) {
            if(map[i][j] == 9) {
                findAllPaths(map, i, j, ratingMap)
            }
        }
    }

    return ratingMap.values.sum()
}

private fun findAllPaths(map: List<List<Int>>, x: Int, y: Int, ratingMap: MutableMap<Pair<Int,Int>, Int>) {

    if(map[x][y] == 0) {
        ratingMap[Pair(x, y)] = ratingMap.getOrDefault(Pair(x,y), 0) + 1
        return
    }

    for(dir in directions) {
        val (dx, dy) = dir
        if(x+dx in map.indices && y+dy in map[0].indices) {
            if(map[x+dx][y+dy] == map[x][y] - 1) {
                findAllPaths(map, x+dx, y+dy, ratingMap)
            }
        }
    }
}