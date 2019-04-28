package com.example.tictactoe

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "results")
data class ResultRoom(
    @PrimaryKey @ColumnInfo(name = "player_name") val player_name: String,
    @ColumnInfo(name = "player_wins") val matchWins: Int
)

