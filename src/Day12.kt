@file:Suppress("DuplicatedCode")

import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

private val directions = listOf(
    0 to 1,
    1 to 0,
    0 to -1,
    -1 to 0
)

fun main() {
    val input = readInput("inputs/day12.txt")
    val inputMatrix: List<List<Char>> = input.map { it.toList() }

    println(part1(inputMatrix))
    println(part2(inputMatrix))
}

private fun part1(map: List<List<Char>>): Int {
    var sol = 0
    val regionIdMap: MutableList<MutableList<Int>> = MutableList(map.size) { MutableList(map[0].size) { -1 } }
    var newId = 1

    for (i in map.indices) {
        for (j in map[0].indices) {
            if(regionIdMap[i][j] == -1) {
                updateRegionIdMap(i, j, map, regionIdMap, newId)
                newId++
            }
        }
    }
    regionIdMap.flatten().distinct().forEach {
        sol += calculateRegionValue(it, regionIdMap)
    }
    return sol
}

private fun updateRegionIdMap(i: Int, j: Int, map: List<List<Char>>, regionIdMap: MutableList<MutableList<Int>>, id: Int) {
    val stack = mutableListOf(i to j)
    while (stack.isNotEmpty()) {
        val (x, y) = stack.removeAt(stack.size - 1)
        if (regionIdMap[x][y] == -1) {
            regionIdMap[x][y] = id
            directions.forEach { (dx, dy) ->
                if (x+dx in map.indices && y+dy in map[0].indices && map[x][y] == map[x+dx][y+dy] && regionIdMap[x+dx][y+dy] == -1) {
                    stack.add(x+dx to y+dy)
                }
            }
        }
    }
}

private fun calculateRegionValue(id: Int, regionIdMap: List<List<Int>>): Int {
    var area = 0
    var perimeter = 0

    for (i in regionIdMap.indices) {
        for (j in regionIdMap[0].indices) {
            if (regionIdMap[i][j] == id) {
                area++
                directions.forEach { (dx, dy) ->
                    if (i+dx in regionIdMap.indices && j+dy in regionIdMap[0].indices) {
                        if (regionIdMap[i+dx][j+dy] != id) {
                            perimeter++
                        }
                    } else {
                        perimeter++
                    }
                }
            }
        }
    }
    return area * perimeter
}

// Part 2
private fun part2(map: List<List<Char>>): Int {
    var sol = 0
    val regionIdMap: MutableList<MutableList<Int>> = MutableList(map.size) { MutableList(map[0].size) { -1 } }
    var newId = 1

    for (i in map.indices) {
        for (j in map[0].indices) {
            if(regionIdMap[i][j] == -1) {
                updateRegionIdMap(i, j, map, regionIdMap, newId)
                newId++
            }
        }
    }

    regionIdMap.flatten().distinct().forEach {
        sol += calculateRegionValueWithDiscount(it, regionIdMap)
    }

    return sol
}

private fun calculateRegionValueWithDiscount(id: Int, regionIdMap: List<List<Int>>): Int {

    val region = mutableSetOf<Pair<Int, Int>>()

    for (i in regionIdMap.indices) {
        for (j in regionIdMap[0].indices) {
            if (regionIdMap[i][j] == id) {
                region.add(i to j)
            }
        }
    }

    return region.size * calculateSides(region)
}

private fun calculateSides(region: Set<Pair<Int, Int>>): Int {

    val up = region.minOf { it.first }
    val down = region.maxOf { it.first }
    val left = region.minOf { it.second }
    val right = region.maxOf { it.second }

    var corners = 0

    for (i in up-1..down+1) {
        for (j in left-1..right+1) {
            val points = setOf(
                i to j,
                i+1 to j,
                i to j+1,
                i+1 to j+1
            )
            if (points.subtract(region).size == 1 || points.subtract(region).size == 3) {
                corners++
            } else if (points.subtract(region) == setOf(i to j, i+1 to j+1) || points.subtract(region) == setOf(i+1 to j, i to j+1)) {
                corners += 2
            }
        }
    }

    return corners
}