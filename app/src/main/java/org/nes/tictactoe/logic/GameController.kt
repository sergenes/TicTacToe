package org.nes.tictactoe.logic

/**
 * Created by sergenes on 10/12/18.
 */

enum class Turn {
    PLAYER_O, PLAYER_X
}

enum class Status {
    GAME, WON_O, WON_X, DRAW
}

class GameController {

    private var currentTurn = Turn.PLAYER_O

    private var movesCount: Int = 0

    private val gameBoard = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    fun nextMove() {
        movesCount += 1
        currentTurn = if (currentTurn == Turn.PLAYER_O) {
            Turn.PLAYER_X
        } else {
            Turn.PLAYER_O
        }
    }

    fun movesCount(): Int {
        return movesCount
    }

    fun currentTurn(): Turn {
        return currentTurn
    }

    fun boardSize(): Int {
        return gameBoard.size
    }

    fun setBoardValue(index: Int, value: Int) {
        gameBoard[index] = value
    }

    /**
     * Returns a Tuple that contain current status and range of board's cells
     * that should be high lighted in case one of the players won.
     * <p>
     * 4 in row (vertical/horizontal/diagonal), 4 corners, or a 2x2 box
     * slice gameBoard array by 4 elements
     * calculate the sum of these elements
     * if sum == 4 "O" player won, if sum == -4 "X" player won
     *
     * @return      the Pair of status and range of cells
     * @see         check4InBox, check4InDiagonal, check4InColumn, check4InRow
     */
    fun checkStatus(): Pair<Status, Iterable<Int>> {
        if (movesCount >= gameBoard.size) {
            return Pair(Status.DRAW, 0..0)
        }

        val (statusRow, rangeRow) = check4InRow()
        if (statusRow != Status.GAME) {
            return Pair(statusRow, rangeRow)
        }
        val (statusDiagonal, rangeDiagonal) = check4InDiagonal()
        if (statusDiagonal != Status.GAME) {
            return Pair(statusDiagonal, rangeDiagonal)
        }
        val (statusColumn, rangeColumn) = check4InColumn()
        if (statusColumn != Status.GAME) {
            return Pair(statusColumn, rangeColumn)
        }
        return check4InBox()

    }

    // scan 2 diagonals
    // |0 |1 |2 |3 |
    // |4 |5 |6 |7 |
    // |8 |9 |10|11|
    // |12|13|14|15|
    private fun check4InDiagonal(): Pair<Status, Iterable<Int>> {
        var range = listOf(0, 5, 10, 15)
        var check4 = gameBoard.slice(range)
        if (check4.sum() == 4) {
            return Pair(Status.WON_O, range)
        } else if (check4.sum() == -4) {
            return Pair(Status.WON_X, range)
        }
        range = listOf(3, 6, 9, 12)
        check4 = gameBoard.slice(range)
        if (check4.sum() == 4) {
            return Pair(Status.WON_O, range)
        } else if (check4.sum() == -4) {
            return Pair(Status.WON_X, range)
        }

        return Pair(Status.GAME, 0..0)
    }

    // scan 4 times with the frame 1x4, idx is x-axis offset
    // |0 |1 |2 |3 |
    // |4 |5 |6 |7 |
    // |8 |9 |10|11|
    // |12|13|14|15|
    private fun check4InColumn(): Pair<Status, Iterable<Int>> {
        for (idx in 0..3) {
            val range = listOf(idx, idx + 4, idx + 8, idx + 12)
            val check4 = gameBoard.slice(range)
            if (check4.sum() == 4) {
                return Pair(Status.WON_O, range)
            } else if (check4.sum() == -4) {
                return Pair(Status.WON_X, range)
            }

        }
        return Pair(Status.GAME, 0..0)
    }

    // scan 4 times with the frame 4x1, idx is y-axis offset
    // |0 |1 |2 |3 |
    // |4 |5 |6 |7 |
    // |8 |9 |10|11|
    // |12|13|14|15|
    private fun check4InRow(): Pair<Status, Iterable<Int>> {
        for (idx in listOf(0, 4, 8, 12)) {
            val range = listOf(idx, idx + 1, idx + 2, idx + 3)
            val check4 = gameBoard.slice(range)
            if (check4.sum() == 4) {
                return Pair(Status.WON_O, range)
            } else if (check4.sum() == -4) {
                return Pair(Status.WON_X, range)
            }

        }
        return Pair(Status.GAME, 0..0)
    }

    // scan 9 times with the frame 2x2
    // |0 |1 |2 |3 |
    // |4 |5 |6 |7 |
    // |8 |9 |10|11|
    // |12|13|14|15|
    private fun check4InBox(): Pair<Status, Iterable<Int>> {
        for (idx in 0..2) {
            val range = listOf(idx, idx + 1, idx + 4, idx + 5)
            val check4 = gameBoard.slice(range)
            if (check4.sum() == 4) {
                return Pair(Status.WON_O, range)
            } else if (check4.sum() == -4) {
                return Pair(Status.WON_X, range)
            }
        }
        for (idx in 4..6) {
            val range = listOf(idx, idx + 1, idx + 4, idx + 5)
            val check4 = gameBoard.slice(range)
            if (check4.sum() == 4) {
                return Pair(Status.WON_O, range)
            } else if (check4.sum() == -4) {
                return Pair(Status.WON_X, range)
            }
        }
        for (idx in 8..10) {
            val range = listOf(idx, idx + 1, idx + 4, idx + 5)
            val check4 = gameBoard.slice(range)
            if (check4.sum() == 4) {
                return Pair(Status.WON_O, range)
            } else if (check4.sum() == -4) {
                return Pair(Status.WON_X, range)
            }
        }
        return Pair(Status.GAME, 0..0)
    }

    fun clearTheBoard() {
        currentTurn = Turn.PLAYER_O
        movesCount = 0
        gameBoard.fill(0)
    }
}