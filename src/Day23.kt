import java.sql.Connection
import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day23.txt").map { it.split("-") }
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<List<String>>): Int {


    val connectionsMap = mutableMapOf<String, MutableList<String>>()

    for (line in input) {
        val first = line[0]
        val second = line[1]
        connectionsMap.getOrPut(first) { mutableListOf() }.add(second)
        connectionsMap.getOrPut(second) { mutableListOf() }.add(first)
    }

    var sol = 0
    var duplicated = 0

    for (key in connectionsMap.keys) {
        if(key.startsWith("t")) {
            val values = connectionsMap[key]!!
            for (i in 0..values.size-2) {
                for (j in i+1..values.size-1) {
                    if (connectionsMap[values[i]]!!.contains(values[j])) {
                        sol++
                        if (values[i].startsWith("t")) {
                            duplicated++
                        }
                        if (values[j].startsWith("t")) {
                            duplicated++
                        }
                    }
                }
            }
        }
    }

    return sol - duplicated / 2
}

private fun part2(input: List<List<String>>): String {

    val connectionsMap = mutableMapOf<String, MutableList<String>>()

    for (line in input) {
        val first = line[0]
        val second = line[1]
        connectionsMap.getOrPut(first) { mutableListOf() }.add(second)
        connectionsMap.getOrPut(second) { mutableListOf() }.add(first)
    }

    val result = mutableSetOf<Set<String>>()
    connectionsMap.keys.forEach { result += setOf(it) }
    while (result.size > 1) {
        val newResult = mutableSetOf<Set<String>>()
        val resultList = result.toList()
        resultList.forEach { node ->
            connectionsMap.filter { (key,value) -> key !in node && value.containsAll(node) }
                .forEach { newResult += node + it.key }
        }
        result.clear()
        result += newResult
    }
    return result.first().sorted().joinToString(",")
}