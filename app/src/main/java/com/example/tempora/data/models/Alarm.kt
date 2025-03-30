package com.example.tempora.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.UUID

@Entity(tableName = "Alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Auto-generated ID
    val selectedDate: String,
    val selectedTime: String
)