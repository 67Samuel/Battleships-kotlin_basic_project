@file:JvmName("BattleshipKt")

package com.example.kotlinplayground

import java.util.*


/**
* BattleShips
 *
 * Set-up: - There are 2 human players
 *         - Player 1 will decide how many battleships they will play with and the sizes of the ships
 *         - Players will take turns to place their battleships
 *         - Players will choose coordinates on a map to fire at in turn
 *         - The player who sinks all his opponent's battleships first wins
 *
 * Rules: - There are no points; there are only winners and losers
 *        - Players will know when any ship has been hit; the board will display it
 *        - The status of the player and his opponent's ships will be shown as well
 *        - Coordinates will be shown in the style of chess
* */
@ExperimentalStdlibApi
fun main() {
    playBattleships()
}

@ExperimentalStdlibApi
fun playBattleships() {
    var gameOver = false
    val startGame = startingProcedure()
    if (!startGame) {
        return
    } else {
        val helper = Helper()
        val (player1, player2) = setUp()
        helper.clearScreen(15)
        while (!gameOver) {
            print("Player 1: Press enter to make your attack")
            readLine()
            helper.clearScreen()
            when (playerTurn(player1, player2, helper)) {
                true -> println("A hit!".pretty())
                false -> println("\nAlas, you missed!".pretty())
                null -> {
                    println("Player 1 has won!".pretty())
                    gameOver = true
                    continue
                }
            }
            print("Press enter to end your turn".pretty())
            readLine()
            helper.clearScreen(40)
            print("Player 2: Press enter to make your attack")
            readLine()
            when (playerTurn(player2, player1, helper)) {
                true -> println("A hit!".pretty())
                false -> println("\nAlas, you missed!".pretty())
                null -> {
                    println("Player 2 has won!".pretty())
                    gameOver = true
                    continue
                }
            }
            print("Press enter to end your turn".pretty())
            readLine()
            helper.clearScreen(40)
        }
    }

}

fun playerTurn(attacker: Player, defender: Player, helper: Helper): Boolean? {
    // show boards
    println("\nYour board:\n")
    helper.printPrivateBoard(attacker)
    println("=====================================")
    println("\nAttack board:\n")
    defender.updatePublicBoard()
    helper.printPublicBoard(defender)

    println("Player ${attacker.index}, where would you like to strike?".pretty())
    print("Enter attack coordinate: ")
    val attackCoordinate = readLine()

    if (attackCoordinate == null) {
        helper.clearScreen()
        println("Invalid attack coordinate, try again".pretty())
        println("(eg. type 'a1' to fire missile at coordinate a1)".pretty())
        return playerTurn(attacker, defender, helper)
    }
    return if (helper.isCoordinateValid(attackCoordinate)) {
        defender.checkHit(attackCoordinate)
    } else {
        helper.clearScreen()
        println("Invalid attack coordinate, try again".pretty())
        println("(eg. type 'a1' to fire missile at coordinate a1)".pretty())
        playerTurn(attacker, defender, helper)
    }
}

@ExperimentalStdlibApi
fun setUp(): List<Player> {
    var numberOfShips: Int?
    var listOfShipSizes = mutableListOf<Int>()

    do {
        println("Player 1, choose how many ships you and your opponent will be battling with!".pretty())
        print("Number of ships: ")
        numberOfShips = readLine()?.toIntOrNull()
        if (numberOfShips == null || numberOfShips < 1) {
            println("Please enter a valid number more than 0".pretty())
        } else if (numberOfShips > 6) {
            println("You can only build at most 6 ships".pretty())
        } else {
            println("Building your ships...".pretty())
        }
    } while (numberOfShips == null || numberOfShips < 1)

    for (shipIndex in 1..numberOfShips) {
        var validInput = false
        while (!validInput) {
            print("Enter the size of ship $shipIndex: ")
            val size = readLine()?.toIntOrNull()
            if (size != null && size > 0) {
                listOfShipSizes.add(size)
                validInput = true
            } else {
                println("Invalid ship size!".pretty())
            }
        }
    }
    val shipMap = (1..numberOfShips).toList().associateBy({it}, {listOfShipSizes[it-1]}) as MutableMap<Int, Int>
    val player1 = Player(shipMap, 1)
    val player2 = Player(shipMap, 2)
    Board(numberOfShips, listOfShipSizes, player1, player2)

    return listOf(player1, player2)
}

fun startingProcedure(): Boolean {
    println("**************** WELCOME TO BATTLESHIPS! ****************\n")

    do {
        print("(y/n) Do you want to play: ")
        val keyEntered = readLine().toString().toLowerCase(Locale.ROOT)
        if (keyEntered == "n") {
            println("Stopping game now!".pretty())
            return false
        } else if (keyEntered == "y") {
            return true
        }
    } while (keyEntered != "n" || keyEntered != "p")

    return false
}

fun String.pretty(): String {
    return "> $this <"
}