package com.example.mobiory.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mobiory.data.model.Claim
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.model.EventWithClaims
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    /*@Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Event>>
*/
    @Transaction
    @Query("SELECT * FROM events")
    fun getAllEventsWithClaims(): Flow<List<EventWithClaims>>
    @Query("SELECT * FROM events")
    fun getAllEventsFlow(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: Claim)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClaims(events: List<Claim>)

    @Query("DELETE FROM events")
    fun deleteAll()

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getById(eventId: Int): Event?

    @Query("SELECT EXISTS(SELECT 1 FROM events WHERE eventId = :eventId)")
    suspend fun exists(eventId: Int): Boolean

}