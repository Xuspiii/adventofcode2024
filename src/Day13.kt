import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.min

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day13.txt")

    println(part1(input))
    println(part2(input))
}

// Part 1
private fun part1(input: List<String>): Long {
    var sol = 0L
    val joinedInput = input.joinToString("\n")
    val casesArray = joinedInput.split("\n\n")
    for (inputCase in casesArray) {
        val case: Case = parseCase(inputCase)
        sol += solveCaseByEquation(case.buttonAmoves, case.buttonBmoves, case.prize)
    }
    return sol
}

private fun parseCase(case: String): Case {
    val caseArray = case.split("\n")
    val buttonAmovesString = caseArray[0].substringAfter("Button A: ").split(", ")
    val buttonAmoves = buttonAmovesString[0].substring(2).toLong() to buttonAmovesString[1].substring(2).toLong()

    val buttonBmovesString = caseArray[1].substringAfter("Button B: ").split(", ")
    val buttonBmoves = buttonBmovesString[0].substring(2).toLong() to buttonBmovesString[1].substring(2).toLong()

    val prizeString = caseArray[2].substringAfter("Prize: ").split(", ")
    val prize = prizeString[0].substring(2).toLong() to prizeString[1].substring(2).toLong()

    return Case(buttonAmoves, buttonBmoves, prize)
}

private class Case(val buttonAmoves: Pair<Long, Long>, val buttonBmoves: Pair<Long, Long>, var prize: Pair<Long, Long>) {}

private fun solveCase(buttonAmoves: Pair<Long, Long>, buttonBmoves: Pair<Long, Long>, prize: Pair<Long, Long>): Long {
    val memo = mutableMapOf<Pair<Long, Long>, Long>()

    val stack = mutableListOf(Triple(0L, 0L, 0L))
    while (stack.isNotEmpty()) {
        val(x, y, tokens) = stack.removeAt(0)

        if(x > prize.first || y > prize.second) {
            continue
        }

        if (memo.containsKey(x to y)) {
            if(memo[x to y]!! <= tokens) {
                continue
            }
        }
        memo[x to y] = tokens

        stack.add(Triple(x + buttonAmoves.first, y + buttonAmoves.second, tokens + 3))
        stack.add(Triple(x + buttonBmoves.first, y + buttonBmoves.second, tokens + 1))
    }

    return memo.getOrDefault(prize, 0L)
}

// Part 2
private fun part2(input: List<String>): Long {
    var sol = 0L
    val joinedInput = input.joinToString("\n")
    val casesArray = joinedInput.split("\n\n")
    for (inputCase in casesArray) {
        val case: Case = parseCase(inputCase)
        case.prize = case.prize.first + 10000000000000 to case.prize.second + 10000000000000
        sol += solveCaseByEquation(case.buttonAmoves, case.buttonBmoves, case.prize)
    }
    return sol
}

private fun solveCaseByEquation(buttonAmoves: Pair<Long, Long>, buttonBmoves: Pair<Long, Long>, prize: Pair<Long, Long>): Long {

    // If it has infinite solutions, A and B must be linearly dependent, so the optimal is pressing just A or just B
    if(prize.first % buttonBmoves.first == 0L && prize.second % buttonBmoves.second == 0L
        && prize.first / buttonBmoves.first == prize.second / buttonBmoves.second
        && prize.first % buttonAmoves.first == 0L && prize.second % buttonAmoves.second == 0L
        && prize.first / buttonAmoves.first == prize.second / buttonAmoves.second) {
        return min(prize.first / buttonBmoves.first, (prize.first / buttonAmoves.first) * 3 )
    }

    val (x1, y1) = buttonAmoves
    val (x2, y2) = buttonBmoves
    val (x, y) = prize

    val D = x1 * y2 - x2 * y1
    val Dx = x * y2 - x2 * y
    val Dy = x1 * y - x * y1

    // Check if the solutions are integers
    if(Dx % D != 0L || Dy % D != 0L) {
        return 0
    }

    val A = Dx / D
    val B = Dy / D

    // Check if the solutions are positive
    if(A >= 0 && B >= 0) {
        return A*3 + B
    }

    return 0
}