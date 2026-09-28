package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.PromptEntity
import com.example.data.model.*
import com.example.data.repository.PromptRepository
import com.example.engine.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

data class EngineUiState(
    val sceneState: SceneState = SceneState(),
    val evaluationResult: EvaluationResult = ConstraintEngine.evaluate(SceneState()),
    val compiledPrompt: CompiledPrompt = PromptCompiler.compile(SceneState()),
    val autoCorrectEnabled: Boolean = true,
    val selectedTab: Int = 0, // 0: Builder, 1: Constraints & Physics, 2: Compiled Prompt, 3: Library & History
    val builderCategoryIndex: Int = 0, // Sub-category inside Builder
    val isCopiedNotificationVisible: Boolean = false,
    val infoMessage: String? = null
)

class PromptEngineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PromptRepository

    private val _uiState = MutableStateFlow(EngineUiState())
    val uiState: StateFlow<EngineUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PromptRepository(db.promptDao())
    }

    val savedPrompts: StateFlow<List<PromptEntity>> = repository.allPrompts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun selectBuilderCategory(index: Int) {
        _uiState.update { it.copy(builderCategoryIndex = index) }
    }

    fun updateState(transform: (SceneState) -> SceneState) {
        val current = _uiState.value.sceneState
        val newState = transform(current)
        recalculate(newState)
    }

    fun onCaptureTypeSelected(type: CaptureType) {
        val updated = ConstraintEngine.onCaptureTypeChanged(_uiState.value.sceneState, type)
        recalculate(updated)
    }

    fun onSceneContextSelected(context: SceneContext) {
        val updated = ConstraintEngine.onSceneContextChanged(_uiState.value.sceneState, context)
        recalculate(updated)
    }

    fun setHandState(isLeft: Boolean, handState: HandState) {
        val current = _uiState.value.sceneState
        val updated = if (isLeft) {
            current.copy(leftHand = handState)
        } else {
            current.copy(rightHand = handState)
        }
        recalculate(updated)
    }

    fun setTargetModel(model: TargetModel) {
        val updated = _uiState.value.sceneState.copy(targetModel = model)
        recalculate(updated)
    }

    fun randomizeSeed() {
        val newSeed = Random.nextLong(100000L, 9999999L)
        val updated = _uiState.value.sceneState.copy(seed = newSeed)
        recalculate(updated)
    }

    fun setSeed(newSeed: Long) {
        val updated = _uiState.value.sceneState.copy(seed = newSeed)
        recalculate(updated)
    }

    fun loadPreset(preset: ScenePreset) {
        recalculate(preset.state)
        _uiState.update {
            it.copy(
                infoMessage = "Loaded preset: ${preset.titleEn}",
                selectedTab = 0
            )
        }
    }

    fun toggleAutoCorrect() {
        val newAutoCorrect = !_uiState.value.autoCorrectEnabled
        _uiState.update { it.copy(autoCorrectEnabled = newAutoCorrect) }
        recalculate(_uiState.value.sceneState, autoCorrect = newAutoCorrect)
    }

    fun saveCurrentPrompt() {
        val currentCompiled = _uiState.value.compiledPrompt
        val currentState = _uiState.value.sceneState
        viewModelScope.launch {
            val entity = PromptEntity(
                title = currentCompiled.title,
                captureType = currentState.captureType.labelEn,
                sceneContext = currentState.sceneContext.labelEn,
                targetModel = currentState.targetModel.labelEn,
                seed = currentState.seed,
                fullPrompt = currentCompiled.fullPrompt
            )
            repository.savePrompt(entity)
            _uiState.update { it.copy(infoMessage = "Saved prompt to local library!") }
        }
    }

    fun deletePrompt(id: Long) {
        viewModelScope.launch {
            repository.deletePrompt(id)
        }
    }

    fun toggleFavorite(id: Long, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !currentFav)
        }
    }

    fun clearInfoMessage() {
        _uiState.update { it.copy(infoMessage = null) }
    }

    private fun recalculate(state: SceneState, autoCorrect: Boolean = _uiState.value.autoCorrectEnabled) {
        val eval = ConstraintEngine.evaluate(state, autoCorrect = autoCorrect)
        val activeState = if (autoCorrect) eval.validatedState else state
        val compiled = PromptCompiler.compile(activeState)
        _uiState.update {
            it.copy(
                sceneState = activeState,
                evaluationResult = eval,
                compiledPrompt = compiled
            )
        }
    }
}
