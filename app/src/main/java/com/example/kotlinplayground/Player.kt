package com.example.kotlinplayground

class Player(private var shipMap: MutableMap<Int, Int>, val index: Int) {
    private val helper = Helper()
    var board = Array(7) { Array(9) {0} }
    var privateBoard = ""
    var publicBoard = ""
    var privateBoardBody = Array(7) {"~ ~ ~ ~ ~ ~ ~ ~ ~"}
    private var publicBoardBody = Array(7) {"~ ~ ~ ~ ~ ~ ~ ~ ~"}

    init {
        updatePrivateBoard()
        updatePublicBoard()
    }

    fun checkHit(coordinate: String): Boolean? {
        val colIndex = helper.lettersToNumbers[coordinate[0]]!!-1
        val rowIndex = Integer.valueOf(coordinate[1].toString())-1

        when (val shipIndex = board[rowIndex][colIndex]) {
            0 -> {
                publicBoardBody[rowIndex] = publicBoardBody[rowIndex].substring(0, colIndex*2) + '-' +
                        publicBoardBody[rowIndex].substring(colIndex*2+1)
                return false
            } // miss
            in 1..7 -> {
                board[rowIndex][colIndex] *= -1
                privateBoardBody[rowIndex] = privateBoardBody[rowIndex].substring(0, colIndex*2) + 'x' +
                        privateBoardBody[rowIndex].substring(colIndex*2+1)
                publicBoardBody[rowIndex] = publicBoardBody[rowIndex].substring(0, colIndex*2) + 'x' +
                        publicBoardBody[rowIndex].substring(colIndex*2+1)
                updatePrivateBoard()
                shipMap[shipIndex] = shipMap[shipIndex]?.minus(1)!!
                if (isShipDestroyed(shipIndex)) {
                    if (hasLost()) {
                        return null
                    }
                }
            }
        }
        return true
    }

    private fun hasLost(): Boolean {
        if (shipMap.values.all { it <= 0 }) {
            return true
        }
        return false
    }

    private fun isShipDestroyed(shipIndex: Int): Boolean {
        if (shipMap[shipIndex] == 0) {
            return true
        }
        return false
    }

    fun updatePrivateBoard() {
        privateBoard = "          a b c d e f g h i\n"
        for (i in 1..7) {
            privateBoard += "        $i "
            privateBoard += privateBoardBody[i-1]
            privateBoard += "\n"
        }
    }

    fun updatePublicBoard() {
        publicBoard = "          a b c d e f g h i\n"
        for (i in 1..7) {
            publicBoard += "        $i "
            publicBoard += publicBoardBody[i-1]
            publicBoard += "\n"
        }
    }
}