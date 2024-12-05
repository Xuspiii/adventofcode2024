import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val rulesInput = readInput("inputs/day05rules.txt")
    val rulesMatrix: List<Pair<Int, Int>> = rulesInput.map {
        val (first, second) = it.split("|").map(String::toInt)
        first to second
    }

    val pagesInput = readInput("inputs/day05pages.txt")
    val pagesMatrix: List<List<Int>> = pagesInput.map { it.split(",").map(String::toInt) }

    println(part1(rulesMatrix, pagesMatrix))
}

private fun part1(input: List<Pair<Int, Int>>, pages: List<List<Int>>): Int {
    var sol = 0
    val mutableInput = input.toMutableList()
    val pagesToOrder: MutableSet<Int> = input.flatMap { listOf(it.first, it.second) }.toMutableSet()
    val orderedPages: MutableList<Int> = mutableListOf()

    while (mutableInput.isNotEmpty() && pagesToOrder.isNotEmpty()) {
        val inputSecondPart = mutableInput.map { it.second }.toSet()
        println(pagesToOrder.toList().sorted())
        println(inputSecondPart.toList().sorted())
        val nextPage = pagesToOrder.subtract(inputSecondPart).first()
        orderedPages.add(nextPage)
        pagesToOrder.remove(nextPage)
        mutableInput.removeIf { it.first == nextPage || it.second == nextPage }
    }
    orderedPages.addAll(pagesToOrder)

//    var case = 0
    for(pageLine in pages) {
        val filteredPages = orderedPages.filter { it in pageLine }
        if (filteredPages == pageLine) {
            sol += pageLine[(pageLine.size - 1) / 2]
        }
//        println("Case ${++case}: $filteredPages")
    }

    return sol
}