package com.example.tictactoe

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.os.AsyncTask
import android.widget.ArrayAdapter
import java.util.Observer


class MainActivity : AppCompatActivity() {

    private val loginFragment = LoginFragment()
    private val gameFragment = GameFragment()
    private val manager = supportFragmentManager
    private var fragmentTransaction = manager.beginTransaction()
    private lateinit var resultFragment: ResultFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        resultFragment = ResultFragment()

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
