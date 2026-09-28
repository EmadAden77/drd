package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_prompts")
data class PromptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val captureType: String,
    val sceneContext: String,
    val targetModel: String,
    val seed: Long,
    val fullPrompt: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
