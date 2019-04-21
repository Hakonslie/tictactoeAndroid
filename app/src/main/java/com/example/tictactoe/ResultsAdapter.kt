package com.example.tictactoe

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ResultsAdapterViewHolder>() {


    private var resultData: Map<String, Int>? = null


    class ResultsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var resultsTextView: TextView = view.findViewById(R.id.tv_result_data)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ResultsAdapter.ResultsAdapterViewHolder {
        val context = viewGroup.context
        val layoutIdForListItem = R.layout.result_list_item
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, viewGroup, false)
        return ResultsAdapterViewHolder(view)
    }
    override fun onBindViewHolder(resultsAdapterViewHolder: ResultsAdapterViewHolder, position: Int) {
        val currentResultData: List<String> = resultData.map { it.toString().plus(it.toString()) }
        resultsAdapterViewHolder.resultsTextView.text = currentResultData
    }

    override fun getItemCount(): Int {
        return resultData?.size ?: 0
    }
    fun setResultData(resultDataParam: Map<String, Int>) {
        resultData = resultDataParam
        notifyDataSetChanged()
    }

}