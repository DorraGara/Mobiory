package com.example.mobiory.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mobiory.data.local.ClaimConverter
import com.example.mobiory.data.local.EventConverters

@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    @ColumnInfo(name = "eventId")
    val eventId: Int,

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

    @ColumnInfo(name = "sourceId")
    val sourceId: Int?
)

data class Popularity(
    val en: Int,
    val fr: Int
) {
    constructor() : this(-1, -1)
}

