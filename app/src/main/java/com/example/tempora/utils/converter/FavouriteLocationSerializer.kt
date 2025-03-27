package com.example.tempora.utils.converter

import com.example.tempora.data.models.FavouriteLocation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import com.google.gson.Gson

object FavouriteLocationSerializer : KSerializer<FavouriteLocation> {
    private val gson = Gson()

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FavouriteLocation", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: FavouriteLocation) {
        encoder.encodeString(gson.toJson(value))
    }

    override fun deserialize(decoder: Decoder): FavouriteLocation {
        return gson.fromJson(decoder.decodeString(), FavouriteLocation::class.java)
    }
}

