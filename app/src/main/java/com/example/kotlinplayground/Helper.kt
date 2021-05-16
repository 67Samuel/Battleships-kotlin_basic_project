package com.example.kotlinplayground

class Helper {
    val validLetters = ('a'..'i').toList().toCharArray()
    val lettersToNumbers = validLetters.map { it to (it-'a'+1) }.toMap()
    val validNumbers = (1..7).toList().toIntArray()
    private val validCoordinates = mutableListOf<String>()


    init {
        validLetters.forEach {
                letter -> validNumbers.forEach {
                number -> validCoordinates.add("$letter$number")
            }
        }
    }

    fun printPrivateBoard(player: Player) {
        println(player.privateBoard)
    }

    fun printPublicBoard(player: Player) {
        println(player.publicBoard)
    }

    fun isCoordinateValid(coordinate: String): Boolean {
        if (
            coordinate.length == 2
            &&validLetters.contains(coordinate[0])
            && validNumbers.contains(Integer.valueOf(coordinate[1].toString()))
        ) {
            return true
        }
        return false
    }

    fun clearScreen(num: Int=9) {
        for (i in 1..num)
            println()
    }
}