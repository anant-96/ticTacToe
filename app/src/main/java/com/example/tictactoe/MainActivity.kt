package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var playerXTurn = true // true for X , false for 0
    private var turnCount = 0
    private var isAllButtonsDisabled = false // only to handle disable state of button while screen rotation

    private lateinit var buttons: Array<Array<Button>>
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons = arrayOf(
            arrayOf(binding.button1, binding.button2, binding.button3),
            arrayOf(binding.button4, binding.button5, binding.button6),
            arrayOf(binding.button7, binding.button8, binding.button9)
        )
        for (btnRow in buttons) {
            for (btn in btnRow) {
                btn.setOnClickListener(this)
            }
        }
        initializeBoardStatus()
        binding.resetButton.setOnClickListener {
            initializeBoardStatus()
        }
    }

    private fun initializeBoardStatus() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].isEnabled = true
                buttons[i][j].text = ""
            }
        }
        isAllButtonsDisabled = false
        playerXTurn = true
        turnCount = 0
        updateTitle(getString(R.string.player_x_turn))
    }

    private fun updateTitle(title: String) {
        binding.titleDisplay.text = title
        if (title.contains("Won", true)) {
            isAllButtonsDisabled = true
            disableAllButtons()
        }
    }

    private fun disableAllButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].isEnabled = false
            }
        }
    }

    override fun onClick(view: View) {
        val clickedButton = view as Button
        if (clickedButton.text.toString() != "") {
            return
        }
        clickedButton.text = if (playerXTurn) "X" else "O"
        turnCount += 1
        if (checkForWin()) {
            updateTitle(getString(if (playerXTurn) R.string.player_x_won else R.string.player_o_won))
        } else {
            playerXTurn = !playerXTurn
            when {
                turnCount == 9 -> updateTitle("Game draw!")
                playerXTurn -> updateTitle(getString(R.string.player_x_turn))
                else -> updateTitle(getString(R.string.player_o_turn))
            }
        }
    }

    private fun checkForWin(): Boolean {
        val boardStatus = Array(3) { Array(3) { "" } }
        for (i in 0..2) {
            for (j in 0..2) {
                boardStatus[i][j] = buttons[i][j].text.toString()
            }
        }

        //row check
        for (row in 0..2) {
            if (boardStatus[row][0] != "" &&
                boardStatus[row][0] == boardStatus[row][1] &&
                boardStatus[row][0] == boardStatus[row][2]
            ) {
//                buttons[row][0].setBackgroundColor(resources.getColor(R.color.glaucous))
//                buttons[row][1].setBackgroundColor(resources.getColor(R.color.glaucous))
//                buttons[row][2].setBackgroundColor(resources.getColor(R.color.glaucous))
                return true
            }
        }

        //column check
        for (col in 0..2) {
            if (boardStatus[0][col] != ""
                && boardStatus[0][col] == boardStatus[1][col]
                && boardStatus[0][col] == boardStatus[2][col]
            ) {
//                buttons[0][col].setBackgroundColor(resources.getColor(R.color.glaucous))
//                buttons[1][col].setBackgroundColor(resources.getColor(R.color.glaucous))
//                buttons[2][col].setBackgroundColor(resources.getColor(R.color.glaucous))
                return true
            }
        }

        // First diagonal check
        if (boardStatus[0][0] != ""
            && boardStatus[0][0] == boardStatus[1][1]
            && boardStatus[0][0] == boardStatus[2][2]
        ) {
//            buttons[0][0].setBackgroundColor(resources.getColor(R.color.glaucous))
//            buttons[1][1].setBackgroundColor(resources.getColor(R.color.glaucous))
//            buttons[2][2].setBackgroundColor(resources.getColor(R.color.glaucous))
            return true
        }

        // Second diagonal check
        if (boardStatus[0][2] != ""
            && boardStatus[0][2] == boardStatus[1][1]
            && boardStatus[0][2] == boardStatus[2][0]
        ) {
//            buttons[0][2].setBackgroundColor(resources.getColor(R.color.glaucous))
//            buttons[1][1].setBackgroundColor(resources.getColor(R.color.glaucous))
//            buttons[2][0].setBackgroundColor(resources.getColor(R.color.glaucous))
            return true
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("CURR_PLAYER", playerXTurn)
        outState.putBoolean("ALL_BUTTON_DISABLED", isAllButtonsDisabled)
        outState.putInt("TURN_COUNT", turnCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        playerXTurn = savedInstanceState.getBoolean("CURR_PLAYER")
        isAllButtonsDisabled = savedInstanceState.getBoolean("ALL_BUTTON_DISABLED")
        turnCount = savedInstanceState.getInt("TURN_COUNT")
        if (isAllButtonsDisabled){
            disableAllButtons()
        }
    }
}