package org.nes.tictactoe

import android.app.AlertDialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_board.*
import org.nes.tictactoe.logic.GameController
import org.nes.tictactoe.logic.Status
import org.nes.tictactoe.logic.Turn

/**
 * Created by sergenes on 10/12/18.
 */

class BoardActivity : AppCompatActivity() {
    private val TAG = this@BoardActivity.javaClass.simpleName

    private val gameController = GameController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        turnTextView.text = "O"

        newGameButton.setOnClickListener {
            clearTheBoard()
        }
    }


    // onClickListener has added to each button
    // in layout android:onClick="buttonClickHandler"
    // through the style.boardButtonStyle
    fun buttonClickHandler(view: View) {
        Log.d(TAG, "buttonClickHandler=>${view.tag}")
        val buttonTag = (view.tag as String).toInt()

        with((view as Button)) {
            this.isEnabled = false

            if (gameController.currentTurn() == Turn.PLAYER_O) {
                this.text = "O"
                gameController.setBoardValue(buttonTag, 1)
                turnTextView.text = "X"
            } else {
                this.text = "X"
                gameController.setBoardValue(buttonTag, -1)
                turnTextView.text = "O"
            }
        }
        gameController.nextMove()

        // nobody can win before 7th move
        if (gameController.movesCount() > 6) {
            val (status, range) = gameController.checkStatus()
            when (status) {
                Status.DRAW -> {
                    showMessage(getString(R.string.draw_message))
                }
                Status.WON_X -> {
                    showMessage(String.format(getString(R.string.congrats_message), "X"))
                    highLightButtons(true, range)
                }
                Status.WON_O -> {
                    showMessage(String.format(getString(R.string.congrats_message), "O"))
                    highLightButtons(true, range)
                }
                else -> {

                }
            }
        }
    }

    // highLight buttons in range
    private fun highLightButtons(red: Boolean, range: Iterable<Int>) {
        for (tag in range) {
            val button: Button? = gameBoardTableLayout.findViewWithTag<Button>("$tag")
            button?.let {
                val color = if (red) {
                    getResourceColor(R.color.colorAccent)
                } else {
                    getResourceColor(R.color.colorWhite)
                }
                it.setBackgroundColor(color)
            }
        }
    }

    private fun clearTheBoard() {
        gameController.clearTheBoard()
        highLightButtons(false, 0..15)
        turnTextView.text = "O"
        enableBoard(true, true)
    }

    private fun enableBoard(enable: Boolean, andClean:Boolean) {
        var tag = 0
        while (tag < gameController.boardSize()) {
            val button: Button? = gameBoardTableLayout.findViewWithTag<Button>("$tag")
            button?.let {
                it.isEnabled = enable
                if (andClean){
                    it.text = ""
                }
            }
            tag += 1
        }
    }

    private fun showMessage(message: String) {
        val builder = AlertDialog.Builder(this@BoardActivity)

        builder.setTitle("GAME Over")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            enableBoard(false, false)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getResourceColor(color: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(color, null)
        } else {
            resources.getColor(color)
        }
    }
}
