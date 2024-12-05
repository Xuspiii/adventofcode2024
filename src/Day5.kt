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
    println(part2(rulesMatrix, pagesMatrix))
}

// Part 1
private fun part1(rules: List<Pair<Int, Int>>, pages: List<List<Int>>): Int {
    var sol = 0
    for(page in pages) {
        if (isValid(page, rules)) {
            sol += page[(page.size - 1) / 2]
        }
    }
    return sol
}

private fun isValid(page: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
    for (i in page.indices) {
        for (j in i+1..<page.size) {
            if(rules.contains(page[j] to page[i])) {
                return false
            }
        }
    }
    return true
}

// Part 2
private fun part2(rules: List<Pair<Int, Int>>, pages: List<List<Int>>): Int {
    var sol = 0
    for(page in pages) {
        if (!isValid(page, rules)) {
            sol += updatePage(page, rules)
        }
    }
    return sol
}

private fun updatePage(page: List<Int>, rules: List<Pair<Int, Int>>): Int {
    val tempPage = page.toMutableList()
    while (!isValid(tempPage, rules)) {
        for (i in page.indices) {
            for (j in i+1..<page.size) {
                if(rules.contains(tempPage[j] to tempPage[i])) {
                    val temp = tempPage[i]
                    tempPage[i] = tempPage[j]
                    tempPage[j] = temp
                }
            }
        }
    }
    return tempPage[(tempPage.size - 1) / 2]
}