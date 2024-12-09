import kotlin.io.path.Path
import kotlin.io.path.readLines

private fun readInput(filename: String): List<String> {
    return Path(filename).readLines()
}

fun main() {
    val input = readInput("inputs/day09.txt")[0]
    println(part1(input))
    println(part2(input))
}

// Part 1
private fun part1(diskMap: String): Long {
    val disk = diskMapToDisk(diskMap)
    return calculateSum(disk)
}

private fun diskMapToDisk(diskMap: String): List<Long> {
    val disk = mutableListOf<Long>()
    var id = 0L
    var isNextStepFile = true

    for (char in diskMap) {
        val n = char.digitToInt()
        if(isNextStepFile) {
            repeat(n) { disk.add(id) }
            id++
        } else {
            repeat(n) { disk.add(-1) }
        }
        isNextStepFile = !isNextStepFile
    }
    return disk
}

private fun calculateSum(disk: List<Long>): Long {
    var sum = 0L
    var left = 0
    var right = disk.size-1
    var i = 0L

    while (left <= right) {
        if(disk[left] != -1L) {
            sum += i * disk[left]
            left++
            i++
        } else if(disk[right] != -1L) {
            sum += i * disk[right]
            right--
            left++
            i++
        } else {
            right--
        }
    }
    return sum
}

// Part 2
// As in part 2 we have to move blocks of numbers, I represented each block as a list.
private fun part2(diskMap: String): Long {
    val disk = diskMapToDiskMatrix(diskMap)
    val orderedDisk = orderDisk(disk)
    return calculateChecksum(orderedDisk)
}

private fun diskMapToDiskMatrix(diskMap: String): List<List<Long>> {
    val disk = mutableListOf<List<Long>>()
    var id = 0L
    var isNextStepFile = true

    for (char in diskMap) {
        val n = char.digitToInt()
        if(n == 0) { // Avoid empty segments
            isNextStepFile = !isNextStepFile
            continue
        }
        val segment = mutableListOf<Long>()
        if(isNextStepFile) {
            repeat(n) { segment.add(id) }
            id++
        } else {
            repeat(n) { segment.add(-1) }
        }
        disk.add(segment)
        isNextStepFile = !isNextStepFile
    }
    return disk
}

private fun orderDisk(disk: List<List<Long>>): List<List<Long>> {
    val orderedDisk = disk.toMutableList()

    var i = orderedDisk.size-1
    while (i >= 0) {
        if (orderedDisk[i][0] == -1L) {
            i--
            continue
        }
        for (j in 0..i) {
            if (orderedDisk[j].size >= orderedDisk[i].size && orderedDisk[j][0] == -1L) {
                val diff = orderedDisk[j].size - orderedDisk[i].size
                orderedDisk[j] = orderedDisk[i]
                orderedDisk[i] = List(orderedDisk[i].size) { -1L }
                if (diff > 0) {
                    orderedDisk.add(j + 1, List(diff) { -1L })
                    i++
                }
                break
            }
        }
        i--
    }
    return orderedDisk
}

private fun calculateChecksum(disk: List<List<Long>>): Long {
    return disk.flatten().mapIndexed { i, num -> if (num != -1L) i * num else 0L }.sum()
}