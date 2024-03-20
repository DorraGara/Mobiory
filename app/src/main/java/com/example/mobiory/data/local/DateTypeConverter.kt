package com.example.mobiory.data.local

import android.util.Log
import androidx.core.net.ParseException
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTypeConverter {
    @TypeConverter
    fun fromDateToString(date: Date?): String? {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("EN"))
        return date?.let { dateFormatter.format(it) }
    }

    @TypeConverter
    fun fromStringToDate(value: String?): Date? {
        if (value.isNullOrEmpty()) return null
        return try {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("EN"))
            dateFormatter.parse(value)
        } catch (e: ParseException) {
            Log.e("DateParsingError", "Error parsing date: $value", e)
            null
        }
    }
}