package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random
import android.os.CountDownTimer



class GameFragment : Fragment() {

    // TODO: Need to add history (Implement local storage)
    // TODO: Check Android Design principles, Architecture and code standards
    // TODO: Description of solution by text, strengths and weaknesses. Decisions etc.
    // TODO: Model of Architecture and and flow in app
    // TODO: Screenshots
    // TODO: BONUS: Make extra pretty

        lateinit var arrayOfGrids: Array<TextView>
        private var isXTurn: Boolean = true
        private var gameIsRunning: Boolean = false
        private var gameVSAI: Boolean = false
        private var moveNumber: Int = 0
        private var winningConditions: Array<String> = arrayOf("012", "345", "678", "036", "147", "258", "246", "048")
        private val arrayOfSides: Array<Int> = arrayOf(1, 3, 5, 7);
        private val arrayOfCorners: Array<Int> = arrayOf(0, 2, 6, 8);
        private lateinit var textViewPlayerOne: TextView
        private lateinit var textViewPlayerTwo: TextView
        private lateinit var textViewGametimer: TextView
        private lateinit var textGameEnd: TextView
        private lateinit var playerOneString: String
        private lateinit var playerTwoString: String
        private lateinit var gameTimer: GameTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewPlayerOne = view.findViewById(R.id.tv_game_player_one)
        textViewPlayerTwo = view.findViewById(R.id.tv_game_player_two)
        textGameEnd =       view.findViewById(R.id.tv_game_end)
        textViewGametimer = view.findViewById(R.id.tv_gametimer)
        gameTimer = GameTimer(textViewGametimer)
        val restartText =   view.findViewById<TextView>(R.id.tv_game_restart)
        restartText.setOnClickListener { resetTable() }
        initGame()
        arrayOfGrids = loadGameBoard(view)

    }

    class GameTimer(gameTimerTextView: TextView, var gameCounter: Int = 0): CountDownTimer(300000, 1000) {
        private var textViewGameTimer = gameTimerTextView
        override fun onTick(millisUntilFinished: Long) {
            textViewGameTimer.text = gameCounter.toString()
            gameCounter++
        }

        override fun onFinish() {
        }

    }
    private fun initGame() {
        gameTimer.gameCounter = 0
        gameTimer.start()
        playerOneString = arguments?.getString("playerOne").toString().replace("\\s".toRegex(), "")
        playerTwoString = arguments?.getString("playerTwo").toString().replace("\\s".toRegex(), "")

        gameVSAI = playerTwoString == "TTTBot"

        textViewPlayerOne.text = playerOneString
        textViewPlayerTwo.text = playerTwoString

        setTurnPlayerOne()
        moveNumber = 0;
        gameIsRunning = true
    }


    private fun resetTable() {
        gameTimer.cancel()
        gameTimer.gameCounter = 0
        gameTimer.start()
        textViewGametimer.visibility = View.VISIBLE
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
                endGame("playerOne")
            }
            else if(inGrid[Integer.parseInt(current[0].toString())] == "O"
                &&  inGrid[Integer.parseInt(current[1].toString())] == "O"
                &&  inGrid[Integer.parseInt(current[2].toString())] == "O")
            {
                endGame("playerTwo")
            }
        }
       if(gameIsRunning && moveNumber == 9) {
           endGame("draw")
       }

    }

    private fun endGame(winner: String) {
        textGameEnd.visibility = View.VISIBLE
        textViewGametimer.visibility = View.INVISIBLE
        gameTimer.cancel()
        gameTimer.gameCounter = 0
        when(winner) {
            "draw" -> {
                textGameEnd.text = "It's a draw!"
            }
            "playerOne" -> {
                textGameEnd.text = "$playerOneString won!"
            }
            "playerTwo" -> {
                textGameEnd.text = "$playerTwoString won!"
            }
        }


        gameIsRunning = false
    }

    private fun setTurnPlayerOne() {
        textViewPlayerOne.setTextColor(Color.rgb(100, 200, 100))
        textViewPlayerTwo.setTextColor(Color.rgb(200, 100, 100))
        isXTurn = true
    }

    private fun setTurnPlayerTwo() {
        textViewPlayerOne.setTextColor(Color.rgb(200, 100, 100))
        textViewPlayerTwo.setTextColor(Color.rgb(100, 200, 100))
        isXTurn = false

    }

    private fun makeAIMove() {

        val availableMoves: MutableList<String> = mutableListOf();
        val inGrid = arrayOfGrids.map { T -> T.text }
        inGrid.forEachIndexed { i, a -> if(a !== "X" && a !== "O") availableMoves.add(i.toString()) }


        // Corners

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
            }
    }

    }

    // Will always try to draw!
    fun investigateMove(xPositions: List<Int>, oPositions: List<Int>): Int  {

        // Winning condititions: "012", "345", "678", "036", "147", "258", "246", "048"
        val possibleMovesFirstPriority = mutableListOf<Int>()
        val possibleMovesSecondPriority = mutableListOf<Int>()

        for(winCon in winningConditions) {
            val currentConCheck = winCon.chunked(1)
            var conditionsMetX = 0
            var conditionsMetO = 0
            val missingSpotsX: MutableList<Int> = mutableListOf()
            val missingSpotsO: MutableList<Int> = mutableListOf()

            //Can "O" Win?
            for(conPos in currentConCheck) {
                if( oPositions.contains(Integer.parseInt(conPos))) conditionsMetO++
                else missingSpotsO.add(Integer.parseInt(conPos))
            }
            if(conditionsMetO == 2 && !xPositions.contains(missingSpotsO[0])) {
                return missingSpotsO[0]
            }

            //Can "X" Win?
            for (conPos in currentConCheck) {
                if (xPositions.contains(Integer.parseInt(conPos))) conditionsMetX++
                else missingSpotsX.add(Integer.parseInt(conPos))
            }
            // Check if there already are "O"-s there
            if (conditionsMetX == 2 && !oPositions.contains(missingSpotsX[0])) {
                possibleMovesFirstPriority.add(missingSpotsX[0])
            }
            else if (conditionsMetX == 1 && !oPositions.contains(missingSpotsX[0]) && !oPositions.contains(missingSpotsX[1])) {
                possibleMovesSecondPriority.addAll(arrayOf(missingSpotsX[0],missingSpotsX[1]))
            }
        }
        // from: https://stackoverflow.com/questions/47200440/kotlin-how-to-find-number-of-repeated-values-in-a-l
        val betterSecondPriorityMoves: Map<Int, Int> = possibleMovesSecondPriority.groupingBy { it }.eachCount().filter { it.value > 1}
            val chosenMove: Int

        if (possibleMovesFirstPriority.size > 0) {
            chosenMove = possibleMovesFirstPriority[Random.nextInt(0, possibleMovesFirstPriority.size)]
            return chosenMove
        }
        else if (betterSecondPriorityMoves.isNotEmpty()) {
            if(betterSecondPriorityMoves.size == 2) {
                val arrayOfAvailableSides: List<Int> = arrayOfSides.filter { !xPositions.contains(it) && !oPositions.contains(it) }
                chosenMove = arrayOfAvailableSides[(Random.nextInt(0, arrayOfAvailableSides.size))]
                return chosenMove
            }
            chosenMove = betterSecondPriorityMoves.entries.elementAt(Random.nextInt(betterSecondPriorityMoves.size)).key
            return chosenMove
        }
        else if (possibleMovesSecondPriority.size > 0) {
            chosenMove = possibleMovesSecondPriority[Random.nextInt(0, possibleMovesSecondPriority.size)]
            return chosenMove
        }
        else return -1
    }

    //TODO: Add emoji responses from BOT

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
                if(gameIsRunning && gameVSAI) {makeAIMove()}
            } else  {
                textView.text = "O";
                setTurnPlayerOne()
                    }
            moveNumber++

            checkGameStatus();

        }
        }
    }
