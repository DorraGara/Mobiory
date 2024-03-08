package com.example.mobiory.data.local

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date

class DateTypeConverter {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    @TypeConverter
    fun fromDateToString(date: Date?): String? {
        return date?.let { dateFormatter.format(it) }
    }

    @TypeConverter
    fun fromStringToDate(value: String?): Date? {
        return value?.let { dateFormatter.parse(it) }
    }
}