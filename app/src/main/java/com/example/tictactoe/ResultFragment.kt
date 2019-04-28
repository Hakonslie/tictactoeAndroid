package com.example.tictactoe

import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ResultFragment : Fragment() {
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var matchDao: MatchResultDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchDao = AppDatabase.getDatabase(view.context).matchResultDao()

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resultRecyclerView = view.findViewById(R.id.result_history_rv)
        resultRecyclerView.layoutManager = layoutManager
        resultRecyclerView.hasFixedSize()
        resultsAdapter = ResultsAdapter()
        resultRecyclerView.adapter = resultsAdapter

        loadResultsData()

    }

    private fun loadResultsData() {

        class GetAllAsyncTask: AsyncTask<Void, Void, List<ResultRoom>>() {
            override fun doInBackground(vararg params: Void?): List<ResultRoom> {
                return matchDao.getAll()
            }

        }
        val getAAT = GetAllAsyncTask().execute().get()
        resultsAdapter.setResultData(getAAT)

    }



}
