package com.example.mobiory.data.local

import androidx.room.TypeConverter
import com.example.mobiory.data.model.Claim
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.model.Popularity
import org.json.JSONArray
import org.json.JSONObject

class EventConverters {
    @TypeConverter
    fun fromEventString(value: String): Pair<List<Event>,List<Claim>> {
        val jsonObject = JSONObject(value)
        val id = jsonObject.getInt("id")
        val label = jsonObject.getString("label")
        val aliases = jsonObject.getString("aliases")
        val description = jsonObject.getString("description")
        val wikipedia = jsonObject.getString("wikipedia")
        val popularity = fromPopularityString(jsonObject.getString("popularity"))
        val sourceId = jsonObject.getInt("sourceId")
        val claimsJsonArray = jsonObject.getJSONArray("claims")

        val newEvent = Event(id, label, aliases, description, wikipedia, popularity, sourceId)
        val newClaims = mutableListOf<Claim>()
        val newEvents = mutableListOf<Event>()
        newEvents.add(newEvent)

        for (i in 0 until claimsJsonArray.length()) {
            val claimString = claimsJsonArray.getString(i)
            val (claims, childEvent) = ClaimConverter().fromClaimString(claimString)
            claims.forEach { cl -> cl.eventIdParent = id.toLong() }
            newClaims.addAll(claims)
            if (childEvent != null) {
                newEvents.add(childEvent)
            }
        }
        return Pair(newEvents,newClaims)
    }

    @TypeConverter
    fun toEventString(event: Event): String {
        val jsonObject = JSONObject()
        jsonObject.put("id", event.eventId)
        jsonObject.put("label", event.label)
        jsonObject.put("aliases", event.aliases)
        jsonObject.put("description", event.description)
        jsonObject.put("wikipedia", event.wikipedia)
        jsonObject.put("popularity", event.popularity?.let { toPopularityString(it) })
        jsonObject.put("sourceId", event.sourceId)
        /*val claimsJsonArray = JSONArray()
        event.claims?.forEach { claim ->
            val claimString = ClaimConverter().toClaimString(claim)
            claimsJsonArray.put(claimString)
        }
        jsonObject.put("claims", claimsJsonArray)*/
        return jsonObject.toString()
    }

    @TypeConverter
    fun fromPopularityString(value: String): Popularity {
        val jsonObject = JSONObject(value)
        val en = jsonObject.getString("en").toIntOrNull()?:-1
        val fr = jsonObject.getString("fr").toIntOrNull()?:-1
        return Popularity(en, fr)
    }

    @TypeConverter
    fun toPopularityString(popularity: Popularity?): String {
        val jsonObject = JSONObject()
        jsonObject.put("en", popularity?.en?: -1)
        jsonObject.put("fr", popularity?.fr?: -1)
        return jsonObject.toString()
    }
}