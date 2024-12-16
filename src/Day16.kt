import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

private val directions = mapOf(
    "UP" to Pair(-1, 0),
    "RIGHT" to Pair(0, 1),
    "DOWN" to Pair(1, 0),
    "LEFT" to Pair(0, -1)
)

fun main() {
    val inputMap = readInput("inputs/day16.txt")
    val map: List<List<Char>> = inputMap.map { it.toList() }
    println(part1(map))
    println(part2(map))
}

// Part 1
private fun part1(initialMap: List<List<Char>>): Int {
    val initialPosition = (initialMap.size-2 to 1)
    val finalPosition = (1 to initialMap.size-2)
    val map: MutableList<MutableList<Int>> = initialMap.map { row ->
        row.map { if (it == '#') -1 else 0 }.toMutableList()
    }.toMutableList()

    val initialDirection = directions["RIGHT"]!!
    DFS(map, initialDirection, initialPosition, finalPosition)

    return map[finalPosition.first][finalPosition.second]
}

private fun DFS(map: MutableList<MutableList<Int>>, direction: Pair<Int,Int>, position: Pair<Int, Int>, finalPosition: Pair<Int, Int>) {

    if(position == finalPosition) return

    for (dir in directions) {
        val newDirection = dir.value
        if (areOpposite(direction, newDirection)) continue
        val newPosition = Pair(position.first + newDirection.first, position.second + newDirection.second)

        if (newPosition == map.size-2 to 1) continue
        if (map[newPosition.first][newPosition.second] == -1) continue

        val moveValue = if (direction == newDirection) 1 else 1001
        if (map[newPosition.first][newPosition.second] == 0 || map[newPosition.first][newPosition.second] > map[position.first][position.second] + moveValue) {
            map[newPosition.first][newPosition.second] = map[position.first][position.second] + moveValue
            DFS(map, newDirection, newPosition, finalPosition)
        }
    }
}

private fun areOpposite(direction1: Pair<Int, Int>, direction2: Pair<Int, Int>): Boolean {
    return direction1.first == -direction2.first && direction1.second == -direction2.second
}

// Part 2
private fun part2(initialMap: List<List<Char>>): Int {
    val initialPosition = (initialMap.size-2 to 1)
    val finalPosition = (1 to initialMap.size-2)
    val map: MutableList<MutableList<Int>> = initialMap.map { row ->
        row.map { if (it == '#') -1 else 0 }.toMutableList()
    }.toMutableList()

    val initialDirection = directions["RIGHT"]!!
    DFS(map, initialDirection, initialPosition, finalPosition)

    val newMap: MutableList<MutableList<Int>> = initialMap.map { row ->
        row.map { if (it == '#') -1 else 0 }.toMutableList()
    }.toMutableList()

    val pathTiles = mutableSetOf(Pair(initialPosition.first, initialPosition.second))
    getPathTiles(pathTiles, newMap, initialDirection, initialPosition, finalPosition, map[finalPosition.first][finalPosition.second])

    return pathTiles.size
}

private fun getPathTiles(
    pathTiles: MutableSet<Pair<Int, Int>>,
    map: MutableList<MutableList<Int>>,
    direction: Pair<Int, Int>,
    position: Pair<Int, Int>,
    finalPosition: Pair<Int, Int>,
    finalValue: Int,
    currentPath: List<Pair<Int, Int>> = listOf()
) {
    if(position == finalPosition && map[position.first][position.second] == finalValue) {
        pathTiles.addAll(currentPath)
    } else {
        for (dir in directions) {
            val newDirection = dir.value
            if (areOpposite(direction, newDirection)) continue
            val newPosition = Pair(position.first + newDirection.first, position.second + newDirection.second)

            if (newPosition == map.size-2 to 1) continue
            if (map[newPosition.first][newPosition.second] == -1) continue

            val moveValue = if (direction == newDirection) 1 else 1001
            if ((map[newPosition.first][newPosition.second] == 0 || map[newPosition.first][newPosition.second] + 1001 > map[position.first][position.second] + moveValue) &&
                map[position.first][position.second] + moveValue <= finalValue) {
                map[newPosition.first][newPosition.second] = map[position.first][position.second] + moveValue
                getPathTiles(pathTiles, map, newDirection, newPosition, finalPosition, finalValue, currentPath + newPosition)
            }
        }
    }
}
