package com.example.tictactoe

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface MatchResultDao {

    @Query("SELECT * FROM results")
    fun getAll(): List<ResultRoom>

    @Query("SELECT player_wins FROM results WHERE player_name lIKE :name")
    fun getForPlayer(name: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg results: ResultRoom)

        // Inspired by demo6 in class
    @Query("SELECT * FROM results")
    fun getAllResultsLive() : LiveData<List<ResultRoom>>



}