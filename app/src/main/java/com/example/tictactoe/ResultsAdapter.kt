package com.example.tictactoe

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsAdapterViewHolder>() {

    private var resultsList: List<ResultRoom> = listOf();

    class ResultsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var resultsNameView: TextView = view.findViewById(R.id.tv_result_name)
        var resultsWinsView: TextView = view.findViewById(R.id.tv_result_wins)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ResultsAdapter.ResultsAdapterViewHolder {
        val context = viewGroup.context
        val layoutIdForListItem = R.layout.result_list_item
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, viewGroup, false)
        return ResultsAdapterViewHolder(view)
    }
    override fun onBindViewHolder(resultsAdapterViewHolder: ResultsAdapterViewHolder, position: Int) {

        resultsAdapterViewHolder.resultsNameView.text = resultsList[position].player_name
        resultsAdapterViewHolder.resultsWinsView.text = resultsList[position].matchWins.toString()
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    fun setResultData(resultDataParam: List<ResultRoom>) {
        resultsList = resultDataParam.sortedByDescending { it.matchWins }

        notifyDataSetChanged()
    }

}