package com.example.tictactoe

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

interface MatchResultDao {

    @Dao
    interface UserDao {
        @Query("SELECT * FROM results")
        fun getAll(): List<ResultRoom>

        @Insert
        fun insert(vararg results: ResultRoom)

    }
}