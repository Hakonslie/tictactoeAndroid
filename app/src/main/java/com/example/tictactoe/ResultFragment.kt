package com.example.tictactoe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class ResultFragment : Fragment() {
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            resultRecyclerView = view.findViewById(R.id.result_history_rv)
            resultRecyclerView.layoutManager = layoutManager
            resultRecyclerView.hasFixedSize()
            resultsAdapter = ResultsAdapter()
            resultRecyclerView.adapter = resultsAdapter

        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadResultsData()

    }

    private fun loadResultsData() {
        val mapOfResults: Map<String, Int> = mapOf("Johnny" to 5, "Haslan" to 2, "B1keM1ke" to 8)

        resultsAdapter.setResultData(mapOfResults)
    }



}
