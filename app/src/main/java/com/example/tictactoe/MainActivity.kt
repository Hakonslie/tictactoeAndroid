package com.example.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val loginFragment = LoginFragment()
    private val gameFragment = GameFragment()
    private val manager = supportFragmentManager
    private var fragmentTransaction = manager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentTransaction.add(R.id.constraint_main, loginFragment)
        fragmentTransaction.commit()
    }

        fun setGameFragment(playerOne: String, playerTwo: String) {
            fragmentTransaction = manager.beginTransaction()
            val arguments = Bundle()
            arguments.putString("playerOne", (if(playerOne == "") "Anon" else playerOne))
            arguments.putString("playerTwo", (if(playerTwo == "") "Anon" else playerTwo))
            gameFragment.arguments = arguments
            fragmentTransaction.replace(R.id.constraint_main, gameFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

    }
}
