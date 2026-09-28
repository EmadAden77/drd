package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PromptDao {
    @Query("SELECT * FROM saved_prompts ORDER BY timestamp DESC")
    fun getAllPrompts(): Flow<List<PromptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrompt(prompt: PromptEntity): Long

    @Query("DELETE FROM saved_prompts WHERE id = :id")
    suspend fun deletePromptById(id: Long)

    @Query("UPDATE saved_prompts SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: Long, isFav: Boolean)
}
