package org.nes.tictactoe

import org.nes.tictactoe.logic.GameController
import org.nes.tictactoe.logic.Status
import org.nes.tictactoe.logic.Turn
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

// |0 |1 |2 |3 |
// |4 |5 |6 |7 |
// |8 |9 |10|11|
// |12|13|14|15|
class GameControllerSpec : Spek({
    describe("A test for Game Controller") {

        val game by memoized { GameController() }
//        val game = GameController()
        game.clearTheBoard()

        describe("When game begins") {
            it("Game board should be 4 * 4 = 16") {
                assertEquals(expected = 16, actual = game.boardSize())
            }
            it("Should be a turn of player O") {
                assertEquals(expected = Turn.PLAYER_O, actual = game.currentTurn())
            }
            game.setBoardValue(0, 1)
            game.nextMove()
            it("After the first move, should be a turn of player X") {
                assertEquals(expected = Turn.PLAYER_X, actual = game.currentTurn())
            }
            it("And moves count should return 1") {
                assertEquals(expected = 1, actual = game.movesCount())
            }

            describe("When 2 players make 7 moves") {
                game.setBoardValue(1, -1)
                game.nextMove()
                game.setBoardValue(5, 1)
                game.nextMove()
                game.setBoardValue(2, -1)
                game.nextMove()
                game.setBoardValue(10, 1)
                game.nextMove()
                game.setBoardValue(3, -1)
                game.nextMove()
                game.setBoardValue(15, 1)
                game.nextMove()
                it("Should return 7 moves count"){
                    assertEquals(expected = 7, actual = game.movesCount())
                }
                val (status, range) = game.checkStatus()
                it("And O player should win"){
                    assertEquals(expected = Status.WON_O, actual = status)
                }
                it("And the range of cells should be diagonal 0,5,10,15"){
                    assertEquals(expected = listOf(0, 5, 10, 15), actual = range)
                }
            }
        }

    }
})