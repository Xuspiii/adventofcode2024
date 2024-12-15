import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun getDirection(move: Char): Pair<Int, Int> {
    return when (move) {
        '^' -> Pair(-1, 0)
        'v' -> Pair(1, 0)
        '<' -> Pair(0, -1)
        '>' -> Pair(0, 1)
        else -> Pair(0, 0)
    }
}

fun main() {
    val inputMap = readInput("inputs/day15map.txt")
    val map: List<List<Char>> = inputMap.map { it.toList() }
    val moves = readInput("inputs/day15moves.txt").joinToString("")

    println(part1(map, moves))
    println(part2(map, moves))
}

// Part 1
private fun part1(initialMap: List<List<Char>>, moves: String): Int {
    var robotPosition = findInitialPosition(initialMap)
    val map = initialMap.map { it.toMutableList() }.toMutableList()

    val movesStack = moves.toMutableList()
    while (movesStack.isNotEmpty()) {
        val move = movesStack.removeAt(0)
        val direction = getDirection(move)

        if(map[robotPosition.first + direction.first][robotPosition.second + direction.second] == '#') {
            continue
        }

        if(map[robotPosition.first + direction.first][robotPosition.second + direction.second] == '.') {
            map[robotPosition.first][robotPosition.second] = '.'
            robotPosition = Pair(robotPosition.first + direction.first, robotPosition.second + direction.second)
            map[robotPosition.first][robotPosition.second] = '@'
            continue
        }

        if(map[robotPosition.first + direction.first][robotPosition.second + direction.second] == 'O') {
            var boxesToMove = 1
            while (map[robotPosition.first + direction.first * (boxesToMove+1)][robotPosition.second + direction.second * (boxesToMove+1)] == 'O') {
                boxesToMove++
            }
            if(map[robotPosition.first + direction.first * (boxesToMove+1)][robotPosition.second + direction.second * (boxesToMove+1)] == '.') {
                map[robotPosition.first][robotPosition.second] = '.'
                map[robotPosition.first + direction.first * (boxesToMove+1)][robotPosition.second + direction.second * (boxesToMove+1)] = 'O'
                robotPosition = Pair(robotPosition.first + direction.first, robotPosition.second + direction.second)
                map[robotPosition.first][robotPosition.second] = '@'

            }
        }
    }

    for (row in map) {
        println(row)
    }

    var sol = 0
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == 'O') {
                sol += 100*i + j
            }
        }
    }
    return sol
}

private fun findInitialPosition(map: List<List<Char>>): Pair<Int, Int> {
    for (i in map.indices) {
        for (j in map.indices) {
            if (map[i][j] == '@') {
                return Pair(i, j)
            }
        }
    }
    return Pair(0, 0)
}

// Part 2
private fun part2(initialMap: List<List<Char>>, moves: String): Int {

    val map = parseMap(initialMap)
    var robotPosition = findInitialPosition(map)

    val movesStack = moves.toMutableList()
    while (movesStack.isNotEmpty()) {
        val move = movesStack.removeAt(0)
        val direction = getDirection(move)

        if(map[robotPosition.first + direction.first][robotPosition.second + direction.second] == '#') {
            continue
        }

        if(map[robotPosition.first + direction.first][robotPosition.second + direction.second] == '.') {
            map[robotPosition.first][robotPosition.second] = '.'
            robotPosition = Pair(robotPosition.first + direction.first, robotPosition.second + direction.second)
            map[robotPosition.first][robotPosition.second] = '@'
            continue
        }

        if(map[robotPosition.first + direction.first][robotPosition.second + direction.second] == '[' || map[robotPosition.first + direction.first][robotPosition.second + direction.second] == ']') {
            if (direction == getDirection('>') || direction == getDirection('<')) {
                var tilesToMove = 1
                while (map[robotPosition.first + direction.first * (tilesToMove+1)][robotPosition.second + direction.second * (tilesToMove+1)] == '['
                    || map[robotPosition.first + direction.first * (tilesToMove+1)][robotPosition.second + direction.second * (tilesToMove+1)] == ']') {
                    tilesToMove++
                }

                if(map[robotPosition.first + direction.first * (tilesToMove+1)][robotPosition.second + direction.second * (tilesToMove+1)] == '.') {
                    map[robotPosition.first][robotPosition.second] = '.'
                    for (i in tilesToMove downTo  1) {
                        map[robotPosition.first + direction.first * (i+1)][robotPosition.second + direction.second * (i+1)] =
                            map[robotPosition.first + direction.first * (i)][robotPosition.second + direction.second * (i)]
                    }
                    robotPosition = Pair(robotPosition.first + direction.first, robotPosition.second + direction.second)
                    map[robotPosition.first][robotPosition.second] = '@'
                }
            } else {
                var canBeMoved = true
                var coordsToMove: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
                val firstRow = mutableListOf(Pair(robotPosition.first + direction.first, robotPosition.second))

                if(map[robotPosition.first + direction.first][robotPosition.second] == '[') {
                    firstRow.add(Pair(robotPosition.first + direction.first, robotPosition.second + 1))
                } else {
                    firstRow.add(Pair(robotPosition.first + direction.first, robotPosition.second - 1))
                }

                coordsToMove.add(firstRow)

                while (canBeMoved) {
                    val lastRow = coordsToMove.last()
                    val newRow = mutableListOf<Pair<Int, Int>>()
                    for (coord in lastRow) {
                        if (map[coord.first + direction.first][coord.second] == '[') {
                            newRow.add(Pair(coord.first + direction.first, coord.second))
                            newRow.add(Pair(coord.first + direction.first, coord.second + 1))
                        } else if (map[coord.first + direction.first][coord.second] == ']') {
                            newRow.add(Pair(coord.first + direction.first, coord.second))
                            newRow.add(Pair(coord.first + direction.first, coord.second - 1))
                        } else if (map[coord.first + direction.first][coord.second] == '#') {
                            canBeMoved = false
                            break
                        }
                    }
                    if (newRow.isEmpty()) {
                        break
                    }
                    coordsToMove.add(newRow.distinct().toMutableList())
                }

                if(canBeMoved) {
                    while (coordsToMove.isNotEmpty()) {
                        val lastRow = coordsToMove.removeAt(coordsToMove.size - 1)
                        for (coord in lastRow) {
                            map[coord.first + direction.first][coord.second] = map[coord.first][coord.second]
                            map[coord.first][coord.second] = '.'
                        }
                        if (coordsToMove.isEmpty()) {
                            for (coord in lastRow) {
                                map[coord.first][coord.second] = '.'
                            }
                        }
                    }
                    map[robotPosition.first][robotPosition.second] = '.'
                    robotPosition = Pair(robotPosition.first + direction.first, robotPosition.second)
                    map[robotPosition.first][robotPosition.second] = '@'
                }
            }
        }
    }

    var sol = 0
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == '[') {
                sol += 100*i + j
            }
        }
    }
    return sol
}

private fun parseMap(initialMap: List<List<Char>>): MutableList<MutableList<Char>> {
    val newMap: MutableList<MutableList<Char>> = mutableListOf()
    for (row in initialMap) {
        val newRow: MutableList<Char> = mutableListOf()
        for (tile in row) {
            when (tile) {
                '#' -> newRow.addAll(listOf('#', '#'))
                'O' -> newRow.addAll(listOf('[', ']'))
                '.' -> newRow.addAll(listOf('.', '.'))
                '@' -> newRow.addAll(listOf('@', '.'))
            }
        }
        newMap.add(newRow)
    }
    return newMap
}