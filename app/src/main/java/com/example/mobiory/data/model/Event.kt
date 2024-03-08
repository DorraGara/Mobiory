package com.example.mobiory.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

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
    val wikipedia: String,

    @Embedded
    val popularity: Popularity?,

    @ColumnInfo(name = "startDate")
    val startDate: Date?,
    @ColumnInfo(name = "endDate")
    val endDate: Date?,
)

data class Popularity(
    val en: Int,
    val fr: Int
) {
    constructor() : this(-1, -1)
}