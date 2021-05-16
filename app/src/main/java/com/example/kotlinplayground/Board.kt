package com.example.kotlinplayground

@ExperimentalStdlibApi
class Board(
    numberOfShips: Int,
    listOfShipSizes: List<Int>,
    player1: Player,
    player2: Player
) {

    private val helper = Helper()

    // ship placement
    init {
        for ((i, player) in listOf(player1, player2).withIndex()) {
            helper.clearScreen(20)
            println("\n"+"Player ${if (i==0) 2 else 1}, LOOK AWAY!".pretty()+"\n")
            print("Player ${i+1}: Press enter when you are ready to position your ships!")
            readLine()
            for (shipIndex in 1..numberOfShips) {
                helper.clearScreen()
                var shipCreated = false
                val shipSize = listOfShipSizes[shipIndex-1]
                while (!shipCreated) {
                    println("\nYour current board:\n")
                    player.updatePrivateBoard()
                    helper.printPrivateBoard(player)
                    val (startCol, startRow, direction) = getValidInputCoordinates(i+1, shipIndex, shipSize) ?: continue
                    shipCreated = createShip(player, startCol, startRow, direction, shipSize, shipIndex)
                }
            }
            helper.clearScreen(10)
            println("Here are your final ship positions!".pretty())
            player.updatePrivateBoard()
            helper.printPrivateBoard(player)
            println("\n"+"Press enter when you are done!".pretty())
            helper.clearScreen()
            readLine()
            helper.clearScreen()
        }
    }

    private fun createShip(player: Player, startCol: Int, startRow: Int, direction: Int, shipSize: Int, shipIndex: Int): Boolean {
        fun handleError(e: String = ""): Boolean {
            println("Ship cannot fit in the requested position. Try again.".pretty())
            println(e)
            return false
        }
        if (direction == 0) { // up
            if (startRow >= shipSize) {
                try {
                    for (rowIndex in (startRow-shipSize) until startRow) {
                        player.board[rowIndex][startCol-1] = shipIndex
                        player.privateBoardBody[rowIndex] = player.privateBoardBody[rowIndex].substring(0, (startCol-1)*2) + 'o' +
                                player.privateBoardBody[rowIndex].substring((startCol-1)*2+1)
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    return handleError(e.toString())
                }
            } else {
                return handleError("up")
            }
        } else
        if (direction == 1) { // down
            if (startRow + shipSize -1 <= 7) {
                try {
                    for (rowIndex in startRow until (startRow + shipSize)) {
                        player.board[rowIndex-1][startCol-1] = shipIndex
                        player.privateBoardBody[rowIndex-1][(startCol-1)*2]
                        player.privateBoardBody[rowIndex-1] = player.privateBoardBody[rowIndex-1].substring(0, (startCol-1)*2) + 'o' +
                                player.privateBoardBody[rowIndex-1].substring((startCol-1)*2+1)
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    return handleError(e.toString())
                }
            } else {
                return handleError("down")
            }
        } else
        if (direction == 2) { // left
            if (startCol >= shipSize) {
                try {
                    for (colIndex in (startCol-shipSize) until startCol) {
                        player.board[startRow-1][colIndex] = shipIndex
                        player.privateBoardBody[startRow-1][(colIndex)*2]
                        player.privateBoardBody[startRow-1] = player.privateBoardBody[startRow-1].substring(0, (colIndex)*2) + 'o' +
                                player.privateBoardBody[startRow-1].substring((colIndex)*2+1)
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    return handleError(e.toString())
                }
            } else {
                return handleError("left")
            }
        } else
        if (direction == 3) { // right
            if (startCol + shipSize -1 <= 9) {
                try {
                    for (colIndex in startCol until (startCol+shipSize)) {
                        player.board[startRow-1][colIndex-1] = shipIndex
                        player.privateBoardBody[startRow-1][(colIndex-1)*2]
                        player.privateBoardBody[startRow-1] = player.privateBoardBody[startRow-1].substring(0, (colIndex-1)*2) + 'o' +
                                player.privateBoardBody[startRow-1].substring((colIndex-1)*2+1)
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    return handleError(e.toString())
                }
            } else {
                return handleError("right")
            }
        }
        else {
            println("Invalid direction".pretty())
            println("Something went wrong, please contact the developer".pretty())
        }
        return true
    }

    private fun getValidInputCoordinates(i: Int, shipIndex: Int, shipSize: Int): List<Int>? {
        while (true) {
            if (shipSize == 1) {
                println("NOTE: 'a1' results in ship placement at a1".pretty()+"\n")
                print("Player $i, please specify the coordinate of ship $shipIndex: ")
            } else {
                println("NOTE: 'a1a2' results in ship placement from a1 in the direction of a1 -> a2".pretty()+"\n")
                print("Player $i, please specify the start coordinate and next coordinate of ship $shipIndex: ")
            }
            helper.clearScreen()
            val coordinates = readLine() ?: continue
            val directionCoordinate: String = if (coordinates.length == 2 && shipSize == 1) {
                coordinates.slice(0..1)
            } else if (coordinates.length == 4 && shipSize != 1) {
                coordinates.slice(2..3)
            } else {
                println("Invalid coordinate! Try again.".pretty())
                return null
            }
            val startCoordinate = coordinates.slice(0..1)
            if (
                helper.validLetters.contains(startCoordinate[0])
                && helper.validNumbers.contains(Integer.valueOf(startCoordinate[1].toString()))
                && helper.validLetters.contains(directionCoordinate[0])
                && helper.validNumbers.contains(Integer.valueOf(directionCoordinate[1].toString()))
            ) {
                val startCol = helper.lettersToNumbers[startCoordinate[0]]!!
                val startRow = Integer.valueOf(startCoordinate[1].toString())
                val dirCol = helper.lettersToNumbers[directionCoordinate[0]]!!
                val dirRow = Integer.valueOf(directionCoordinate[1].toString())
                if (startCol == dirCol && startRow > dirRow) {
                    return listOf(startCol, startRow, 0) // up
                } else if (startCol == dirCol && startRow < dirRow) {
                    return listOf(startCol, startRow, 1) //  down
                } else if (startCol > dirCol && startRow == dirRow) {
                    return listOf(startCol, startRow, 2) //  left
                } else if (startCol < dirCol && startRow == dirRow) {
                    return listOf(startCol, startRow, 3) // right
                } else if (shipSize == 1) {
                    return listOf(startCol, startRow, 0) // direction does not matter
                } else {
                    println("Invalid direction coordinate. Try again".pretty())
                    return null
                }
            } else {
                println("Invalid input structure. Try again.".pretty())
                return null
            }
        }
    }

}