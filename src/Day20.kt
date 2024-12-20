import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

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
    val inputMap = readInput("inputs/day20.txt")
    val map: List<List<Char>> = inputMap.map { it.toList() }
    println(part1(map))
    println(part2(map))
}

private fun part1(map: List<List<Char>>): Int {
    var initialPosition: Pair<Int, Int> = Pair(0, 0)
    var finalPosition: Pair<Int, Int> = Pair(0, 0)

    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == 'S') {
                initialPosition = Pair(i, j)
            }
            if (map[i][j] == 'E') {
                finalPosition = Pair(i, j)
            }
        }
    }

    val positionValues: MutableMap<Pair<Int, Int>, Int> = mutableMapOf(initialPosition to 0)
    updatePositionValues(map, positionValues, initialPosition, finalPosition)

    var sol = 0
    for (i in 1..map.size-2) {
        for (j in 1..map[i].size-2) {
            if (map[i][j] == '#') {
               for (dir in directions) {
                   if (map[i + dir.first][j + dir.second] != '#' && map[i - dir.first][j - dir.second] != '#') {
                       val cheatValue = positionValues[Pair(i + dir.first, j + dir.second)]!! - positionValues[Pair(i - dir.first, j - dir.second)]!! - 2
                       if (cheatValue >= 100) sol++
                   }
               }
            }
        }
    }

    return sol
}

private fun updatePositionValues(
    map: List<List<Char>>,
    positionValues: MutableMap<Pair<Int, Int>, Int>,
    initialPosition: Pair<Int, Int>,
    finalPosition: Pair<Int, Int>
) {
    var lastPositionAdded = initialPosition
    var lastValueAdded = 0

    while (!positionValues.containsKey(finalPosition)) {
        for (dir in directions) {
            if (map[lastPositionAdded.first + dir.first][lastPositionAdded.second + dir.second] != '#' && !positionValues.containsKey(Pair(lastPositionAdded.first + dir.first, lastPositionAdded.second + dir.second))) {
                val newPosition = Pair(lastPositionAdded.first + dir.first, lastPositionAdded.second + dir.second)
                positionValues[newPosition] = ++lastValueAdded
                lastPositionAdded = newPosition
            }
        }
    }
}

private fun part2(map: List<List<Char>>): Int {
    var initialPosition: Pair<Int, Int> = Pair(0, 0)
    var finalPosition: Pair<Int, Int> = Pair(0, 0)

    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == 'S') {
                initialPosition = Pair(i, j)
            }
            if (map[i][j] == 'E') {
                finalPosition = Pair(i, j)
            }
        }
    }

    val positionValues: MutableMap<Pair<Int, Int>, Int> = mutableMapOf(initialPosition to 0)
    updatePositionValues(map, positionValues, initialPosition, finalPosition)

    var sol = 0
    for (i in 1..map.size-2) {
        for (j in 1..map[i].size-2) {
            if (map[i][j] != '#') {
                sol += findCheats(map, positionValues, i, j)
            }
        }
    }

    return sol
}

private fun findCheats(map: List<List<Char>>, positionValues: MutableMap<Pair<Int, Int>, Int>, i: Int, j: Int): Int {
    var sol = 0

    for (m in i-20..i+20) {
        for (n in j-20..j+20) {
            if (abs(m - i) + abs(n - j) > 20) continue
            if(m in map.indices && n in map[m].indices && map[m][n] != '#') { // Check out of bounds and can be end of cheat
                val cheatValue = positionValues[Pair(m, n)]!! - positionValues[Pair(i, j)]!! - (abs(m - i) + abs(n - j))
                if (cheatValue >= 100) {
                    sol++
                    continue
                }
            }
        }
    }

    return sol
}