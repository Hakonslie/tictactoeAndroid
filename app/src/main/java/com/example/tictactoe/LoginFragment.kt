package com.example.tictactoe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

class LoginFragment : Fragment( ) {
    lateinit var buttonFromLogin: Button
    lateinit var playerOneText: EditText
    lateinit var playerTwoText: EditText
    lateinit var checkBoxAI: CheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonFromLogin = view.findViewById(R.id.btn_from_login)
        playerOneText = view.findViewById(R.id.player_one_name)
        playerTwoText = view.findViewById(R.id.player_two_name)
        checkBoxAI = view.findViewById(R.id.login_checkbox_ai)
        checkBoxAI.setOnClickListener { v -> onCheckboxClicked(v)}
        buttonFromLogin.setOnClickListener { startGame(playerOneText.text.toString(), playerTwoText.text.toString()) }

    }

    // Inspired by: https://developer.android.com/guide/topics/ui/controls/checkbox.html
    private fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            if (view.isChecked)
            {
                playerTwoText.setText(getString(R.string.nameOfBot))
                playerTwoText.isEnabled = false
            }
            else {
                playerTwoText.setText("")
                playerTwoText.isEnabled = true
            }
        }
    }


    private fun startGame(playerOne: String, playerTwo: String) {
        (activity as MainActivity).setGameFragment(playerOne, playerTwo)
    }

}