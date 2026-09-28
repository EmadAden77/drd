package com.example.data.repository

import com.example.data.db.PromptDao
import com.example.data.db.PromptEntity
import kotlinx.coroutines.flow.Flow

class PromptRepository(private val dao: PromptDao) {
    val allPrompts: Flow<List<PromptEntity>> = dao.getAllPrompts()

    suspend fun savePrompt(prompt: PromptEntity): Long {
        return dao.insertPrompt(prompt)
    }

    suspend fun deletePrompt(id: Long) {
        dao.deletePromptById(id)
    }

    suspend fun toggleFavorite(id: Long, isFav: Boolean) {
        dao.toggleFavorite(id, isFav)
    }
}
