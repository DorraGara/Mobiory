package com.example.mobiory.data.local

import androidx.room.TypeConverter
import com.example.mobiory.data.model.Claim
import com.example.mobiory.data.model.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ClaimConverter {
    @TypeConverter
    fun fromString(value: String?): List<Claim>? {
        val listType = object : TypeToken<List<Claim>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListClaim(list: List<Claim>?): String? {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun fromClaimString(value: String): Pair<List<Claim>, Event?> {
        val jsonObject = JSONObject(value)
        val id = jsonObject.getInt("id")
        val verboseName = jsonObject.getString("verboseName")
        val claimValue = jsonObject.getString("value")
        val eventString = jsonObject.optString("item", "")
        val claims = mutableListOf<Claim>()
        if (eventString != "") {
            val (linkedEvent, childClaims) = EventConverters().fromEventString(eventString)
            val claim = Claim(id, verboseName, claimValue, eventIdChild = event.eventId.toLong())
            claims.addAll(childClaims)
            claims.add(claim)
            return Pair(claims, event)
        }
        val claim = Claim(id, verboseName, claimValue)
        claims.add(claim)
        return Pair(claims,null)

    }

    @TypeConverter
    fun toClaimString(claim: Claim): String {
        val jsonObject = JSONObject()
        jsonObject.put("id", claim.claimId)
        jsonObject.put("verboseName", claim.verboseName)
        jsonObject.put("value", claim.value)
        //jsonObject.put("event", EventConverters().toEventString(claim.event))
        return jsonObject.toString()
    }
}