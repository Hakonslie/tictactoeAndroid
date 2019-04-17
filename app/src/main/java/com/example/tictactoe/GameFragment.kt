package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class GameFragment : Fragment() {
        lateinit var arrayOfGrids: Array<TextView>
        var isXTurn: Boolean = true
        var gameIsRunning: Boolean = false
        var gameVSAI: Boolean = false
        var moveNumber: Int = 0
        private var winningConditions: Array<String> = arrayOf("012", "345", "678", "036", "147", "258", "246", "048")
        private lateinit var textViewPlayerOne: TextView
        private lateinit var textViewPlayerTwo: TextView
        private lateinit var textGameEnd: TextView
        private lateinit var playerOneString: String
        private lateinit var playerTwoString: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewPlayerOne = view.findViewById(R.id.tv_game_player_one)
        textViewPlayerTwo = view.findViewById(R.id.tv_game_player_two)
        textGameEnd =       view.findViewById(R.id.tv_game_end)
        val restartText =   view.findViewById<TextView>(R.id.tv_game_restart)
        restartText.setOnClickListener { resetTable() }
        initGame()
        arrayOfGrids = loadGameBoard(view)
    }

    private fun initGame() {
        playerOneString = arguments?.getString("playerOne").toString().replace("\\s".toRegex(), "")
        playerTwoString = arguments?.getString("playerTwo").toString().replace("\\s".toRegex(), "")

        if(playerTwoString == "TTTBot") {
            gameVSAI = true
        }

        textViewPlayerOne.text = playerOneString
        textViewPlayerTwo.text = playerTwoString

        setTurnPlayerOne()
        moveNumber = 0;
        gameIsRunning = true
    }

    private fun resetTable() {
        for(view: TextView in arrayOfGrids) {
            view.text = ""
        }
        textGameEnd.visibility = View.INVISIBLE
        gameIsRunning = true
        moveNumber = 0
        setTurnPlayerOne()
    }

    private fun loadGameBoard(view: View): Array<TextView> {
        val gridViews: Array<TextView> = arrayOf(
            view.findViewById(R.id.grid1),
            view.findViewById(R.id.grid2),
            view.findViewById(R.id.grid3),
            view.findViewById(R.id.grid4),
            view.findViewById(R.id.grid5),
            view.findViewById(R.id.grid6),
            view.findViewById(R.id.grid7),
            view.findViewById(R.id.grid8),
            view.findViewById(R.id.grid9)
            )
        gridViews.map { tV -> tV.setOnClickListener { v -> clickedGrid(v)} }
        return gridViews
    }

    private fun checkGameStatus() {
        val inGrid = arrayOfGrids.map { T -> T.text }
        for (winCon in winningConditions) {
            val current = winCon.chunked(1)
            if(     inGrid[Integer.parseInt(current[0])] == "X"
                &&  inGrid[Integer.parseInt(current[1])] == "X"
                &&  inGrid[Integer.parseInt(current[2])] == "X")
            {
                Log.e("X", "WINNER!")
                endGame("playerOne")
            }
            else if(inGrid[Integer.parseInt(current[0].toString())] == "O"
                &&  inGrid[Integer.parseInt(current[1].toString())] == "O"
                &&  inGrid[Integer.parseInt(current[2].toString())] == "O")
            {
                Log.e("O", "WINNER!")
                endGame("playerTwo")
            }
        }
       if(gameIsRunning && moveNumber == 9) {
           endGame("draw")
       }

    }

    private fun endGame(winner: String) {
        when(winner) {
            "draw" -> {
                textGameEnd.visibility = View.VISIBLE
                textGameEnd.text = "It's a draw!"
            }
            "playerOne" -> {
                textGameEnd.visibility = View.VISIBLE
                textGameEnd.text = "$playerOneString won!"
            }
            "playerTwo" -> {
                textGameEnd.visibility = View.VISIBLE
                textGameEnd.text = "$playerTwoString won!"
            }
        }
        gameIsRunning = false
    }

    private fun setTurnPlayerOne() {
        Log.e("Player", "One")
        textViewPlayerOne.setTextColor(Color.rgb(100, 200, 100))
        textViewPlayerTwo.setTextColor(Color.rgb(200, 100, 100))
        isXTurn = true
    }

    private fun setTurnPlayerTwo() {
        Log.e("Player", "Two")
        textViewPlayerOne.setTextColor(Color.rgb(200, 100, 100))
        textViewPlayerTwo.setTextColor(Color.rgb(100, 200, 100))
        isXTurn = false

    }

    private fun makeAIMove() {
        Log.e("AI", "Making a move")
        var availableMoves: MutableList<String> = mutableListOf();
        val inGrid = arrayOfGrids.map { T -> T.text }
        inGrid.forEachIndexed { i, a -> if(a !== "X" && a !== "O") availableMoves.add(i.toString()) }

         Log.e("inGrid", inGrid.toString())
         Log.e("Mutable", availableMoves.toString())

        if(availableMoves.size != 0)
            clickedGrid(arrayOfGrids[Integer.parseInt(availableMoves[Random.nextInt(0, availableMoves.size)])])

    }

    private fun clickedGrid(view: View) {
        val textView: TextView = view as TextView

        if(!gameIsRunning) Toast.makeText(context, "Game is over!", Toast.LENGTH_SHORT).show()

        else if(textView.text.equals("X") || textView.text.equals("O")) {
            Toast.makeText( context, "Already clicked this",  Toast.LENGTH_SHORT).show()
        }

        else if(gameIsRunning){
            if (isXTurn) {
                textView.text = "X";
                setTurnPlayerTwo()
                if(gameVSAI) {
                    makeAIMove()
                }
            } else  {
                textView.text = "O";
                setTurnPlayerOne()
                    }
            moveNumber++
            if(moveNumber > 4) {
                checkGameStatus();
            }
        }
        }
    }
