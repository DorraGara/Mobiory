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

    //@Query("SELECT * FROM events ORDER BY CASE WHEN startDate IS NULL THEN pointInTime ELSE endDate END ASC")
    @Query("SELECT * FROM events ORDER BY startDate ASC")
    //@Query("SELECT * FROM events ORDER BY startDate, pointInTime ASC")
    fun sortEventsDateASC(): Flow<List<Event>>

    //@Query("SELECT * FROM events ORDER BY CASE WHEN startDate IS NULL THEN pointInTime ELSE endDate END DESC")
    @Query("SELECT * FROM events ORDER BY startDate DESC")
    //@Query("SELECT * FROM events ORDER BY startDate, pointInTime DESC")
    fun sortEventsDateDESC(): Flow<List<Event>>

    //@Query("SELECT * FROM events ORDER BY CASE WHEN popularityEN IS NULL THEN popularityFR END ASC")
    @Query("SELECT * FROM events ORDER BY popularityEN ASC")

    fun sortEventsPopularityASC(): Flow<List<Event>>

    //@Query("SELECT * FROM events ORDER BY CASE WHEN popularityEN IS NULL THEN popularityFR END DESC")

    @Query("SELECT * FROM events ORDER BY popularityEN DESC")

    fun sortEventsPopularityDESC(): Flow<List<Event>>


}