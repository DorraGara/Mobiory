package com.example.mobiory.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "claims")
data class Claim(
    @PrimaryKey
    @ColumnInfo(name = "claimId")
    val claimId: Int,
    @ColumnInfo(name = "verbose_name")
    val verboseName: String,
    @ColumnInfo(name = "value")
    val value: String,
    var eventIdParent: Long? = null,
    var eventIdChild: Long? = null
)
/*{
    constructor() : this(0, "", "")
}*/