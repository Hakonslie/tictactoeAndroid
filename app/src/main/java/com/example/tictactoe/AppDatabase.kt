package com.example.tictactoe

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [MatchResultDao::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MatchResltDao(): MatchResultDao
}