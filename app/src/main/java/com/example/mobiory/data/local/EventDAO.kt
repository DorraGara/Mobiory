package com.example.mobiory.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM events")
    fun getAllEventsFlow(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)

    @Query("DELETE FROM events")
    fun deleteAll()
    @Query("SELECT * FROM events WHERE labelEN LIKE '%' || :searchString || '%' OR labelFR LIKE '%' || :searchString || '%'")
    fun searchEvents(searchString: String): Flow<List<Event>>
}