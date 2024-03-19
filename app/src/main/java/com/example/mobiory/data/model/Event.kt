package com.example.mobiory.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: Int,

    @Embedded
    val label: Label?,

    @Embedded
    val description: Description?,

    @Embedded
    val wikipedia: Wikipedia?,

    @Embedded
    val popularity: Popularity?,

    @ColumnInfo(name = "startDate")
    val startDate: Date?,
    @ColumnInfo(name = "endDate")
    val endDate: Date?,
    @ColumnInfo(name = "pointInTime")
    val pointInTime: Date?,

    @ColumnInfo(name = "country")
    val country: String?,

    val favorite: Boolean,

    val tag: String

)

data class Popularity(
    val popularityEN: Int,
    val popularityFR: Int
)

data class Label(
    val labelEN: String?,
    val labelFR: String?
)

data class Description(
    val descriptionEN: String?,
    val descriptionFR: String?
)

data class Wikipedia(
    val wikipediaEN: String?,
    val wikipediaFRr: String?
)