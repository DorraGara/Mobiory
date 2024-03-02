package com.example.mobiory.data

import android.content.Context
import com.example.mobiory.data.repository.EventRepository

interface AppContainer {
    val eventRepository: EventRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val eventRepository: EventRepository by lazy {
        EventRepository(AppDatabase.getDatabase(context).eventDao())
    }
}