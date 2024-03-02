package com.example.mobiory.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "label")
    val label: String,
    @ColumnInfo(name = "aliases")
    val aliases: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String
    // Autres champs...
)