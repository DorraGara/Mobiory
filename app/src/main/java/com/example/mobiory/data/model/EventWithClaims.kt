package com.example.mobiory.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithClaims (
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "claimId"
    )
    val claims: List<Claim>
)