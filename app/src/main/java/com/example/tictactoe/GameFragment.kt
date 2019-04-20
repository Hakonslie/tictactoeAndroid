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

    // TODO: Need to show seconds past since game started (LiveData)
    // TODO: Need to add history (Implement local storage)
    // TODO: Check Android Design principles, Architecture and code standards
    // TODO: Description of solution by text, strengths and weaknesses. Decisions etc.
    // TODO: Model of Architecture and and flow in app
    // TODO: Screenshots
    // TODO: BONUS: Add difficult AI
    // TODO: BONUS: Make extra pretty

        lateinit var arrayOfGrids: Array<TextView>
        var isXTurn: Boolean = true
        var gameIsRunning: Boolean = false
        var gameVSAI: Boolean = false
        var moveNumber: Int = 0
        private var winningConditions: Array<String> = arrayOf("012", "345", "678", "036", "147", "258", "246", "048")
        private val arrayOfSides: Array<Int> = arrayOf(1, 3, 5, 7);
        private val arrayOfCorners: Array<Int> = arrayOf(0, 2, 6, 8);
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

        val availableMoves: MutableList<String> = mutableListOf();
        val inGrid = arrayOfGrids.map { T -> T.text }
        inGrid.forEachIndexed { i, a -> if(a !== "X" && a !== "O") availableMoves.add(i.toString()) }


        // Corners
        // val arrayOfDangerousCombinations: Array<String> = arrayOf("81", "83", "61", "65", "50", "70", "23", "27");

        // last move
        if(availableMoves.size == 1) {
            clickedGrid(arrayOfGrids[Integer.parseInt(availableMoves[0])]);
        }
        // first move
        else if(availableMoves.size == 8) {
            var xChose = -1
            inGrid.forEachIndexed { i, c -> if(c == "X") xChose = i }
            if(arrayOfCorners.contains(xChose)) { clickedGrid(arrayOfGrids[4]) }
            else if(arrayOfSides.contains(xChose)) { clickedGrid(arrayOfGrids[4]) }
            else if(xChose == 4) { clickedGrid(arrayOfGrids[0]) }
        }
        else if(availableMoves.size != 0)
        {
            val xPos: MutableList<Int> = mutableListOf()
            val oPos: MutableList<Int> = mutableListOf()
            inGrid.forEachIndexed {i, v -> if(v == "X") xPos.add(i) else if (v == "O") oPos.add(i) }
            val investigated = investigateMove(xPos, oPos)
            if (investigated != -1) clickedGrid(arrayOfGrids[investigated])
            else {
                clickedGrid(arrayOfGrids[Integer.parseInt(availableMoves[Random.nextInt(0, availableMoves.size)])])
                Log.e("AI", "I mad a random move :P")
            }
    }

    }

    // Will always try to draw!
    fun investigateMove(xPositions: List<Int>, oPositions: List<Int>): Int  {

        // Winning condititions: "012", "345", "678", "036", "147", "258", "246", "048"
        val possibleMovesFirstPriority = mutableListOf<Int>();
        val possibleMovesSecondPriority = mutableListOf<Int>();
        // Calculate immediate dangers in opponent positioning
        Log.e("xPositions", xPositions.toString())
        Log.e("oPositions", oPositions.toString())
        for(winCon in winningConditions) {
            val currentConCheck = winCon.chunked(1)
            var conditionsMet = 0
            val missingSpots: MutableList<Int> = mutableListOf()
            // Find dangerous combinations
            for (conPos in currentConCheck) {
                if (xPositions.contains(Integer.parseInt(conPos))) conditionsMet++
                else missingSpots.add(Integer.parseInt(conPos))
            }
            // Check if there already are "O"-s there
            if (conditionsMet == 2 && !oPositions.contains(missingSpots[0])) {
                Log.e("AI", "Found a possible first priority move")
                possibleMovesFirstPriority.add(missingSpots[0])
            }
            else if (conditionsMet == 1 && !oPositions.contains(missingSpots[0]) && !oPositions.contains(missingSpots[1])) {
                Log.e("AI", "Found a possible secondary priority move")
                possibleMovesSecondPriority.addAll(arrayOf(missingSpots[0],missingSpots[1]))
            }
        }
        // from: https://stackoverflow.com/questions/47200440/kotlin-how-to-find-number-of-repeated-values-in-a-l
        // Check if there are moves that stop more winning conditions
        val betterSecondPriorityMoves: Map<Int, Int> = possibleMovesSecondPriority.groupingBy { it }.eachCount().filter { it.value > 1}
        Log.e("AI", "Better SecondPriorityMoves: $betterSecondPriorityMoves")
            var chosenMove: Int

        if (possibleMovesFirstPriority.size > 0) {
            chosenMove = possibleMovesFirstPriority[Random.nextInt(0, possibleMovesFirstPriority.size)]
            Log.e("AI", "Going for a first priority move: $chosenMove")
            return chosenMove
        }
        else if (betterSecondPriorityMoves.isNotEmpty()) {
            Log.e("AI", "This looks like a trap!")
            if(betterSecondPriorityMoves.size == 2) {
                return arrayOfSides.map { v -> if(xPositions.contains(v) || oPositions.contains(v))}
            }
            chosenMove = betterSecondPriorityMoves.entries.elementAt(Random.nextInt(betterSecondPriorityMoves.size)).key
            Log.e("AI", "Going for a good second priority move $chosenMove")
            return chosenMove
        }
        else if (possibleMovesSecondPriority.size > 0) {
            chosenMove = possibleMovesSecondPriority[Random.nextInt(0, possibleMovesSecondPriority.size)]
            Log.e("AI", "Going for an ok second priority move $chosenMove")
            return chosenMove
        }
        else return -1
    }

    //TODO: Add emoji responses from BOT
    //TODO: https://developer.android.com/reference/android/os/CountDownTimer

    // Double corner is dangerous

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
                checkGameStatus()
                if(gameIsRunning && gameVSAI)  makeAIMove()
            } else  {
                textView.text = "O";
                setTurnPlayerOne()
                    }
            moveNumber++

            checkGameStatus();

        }
        }
    }
