package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CoherenceStatusBadge
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.PromptEngineViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PromptEngineViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme(darkTheme = true) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val savedPrompts by viewModel.savedPrompts.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                // Trigger snackbar on infoMessage
                LaunchedEffect(uiState.infoMessage) {
                    uiState.infoMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearInfoMessage()
                    }
                }

                // Handle back button: if not on tab 0, go back to Studio Builder
                BackHandler(enabled = uiState.selectedTab != 0) {
                    viewModel.selectTab(0)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = SpaceBlack,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        text = "Realistic Prompt Engine",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "محرك هندسة Prompts للصور الواقعية والفيزياء",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = AmberGlow,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            },
                            actions = {
                                CoherenceStatusBadge(
                                    violations = uiState.evaluationResult.violationsCount,
                                    autoCorrections = uiState.evaluationResult.autoCorrectionsCount,
                                    onClick = { viewModel.selectTab(1) },
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = DeepNavySurface,
                                titleContentColor = TextPrimary
                            ),
                            windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = DeepNavySurface,
                            contentColor = TextPrimary,
                            windowInsets = WindowInsets.navigationBars
                        ) {
                            val items = listOf(
                                NavItem("Studio", "المشهد", Icons.Filled.Tune, Icons.Outlined.Tune, "nav_studio"),
                                NavItem("Physics", "الفيزياء", Icons.Filled.Shield, Icons.Outlined.Shield, "nav_physics"),
                                NavItem("Prompt", "الـPrompt", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome, "nav_prompt"),
                                NavItem("Library", "المحفوظات", Icons.Filled.Bookmarks, Icons.Outlined.Bookmarks, "nav_library")
                            )

                            items.forEachIndexed { index, item ->
                                val selected = uiState.selectedTab == index
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(index) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.labelEn,
                                            tint = if (selected) SpaceBlack else TextSecondary
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.labelEn,
                                            fontSize = 11.sp,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selected) ElectricSky else TextSecondary
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = ElectricSky,
                                        selectedIconColor = SpaceBlack,
                                        unselectedIconColor = TextSecondary,
                                        selectedTextColor = ElectricSky,
                                        unselectedTextColor = TextSecondary
                                    ),
                                    modifier = Modifier.testTag(item.tag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.selectedTab) {
                            0 -> BuilderScreen(
                                uiState = uiState,
                                onCategorySelect = { viewModel.selectBuilderCategory(it) },
                                onCaptureTypeSelect = { viewModel.onCaptureTypeSelected(it) },
                                onSceneContextSelect = { viewModel.onSceneContextSelected(it) },
                                onStateUpdate = { viewModel.updateState(it) },
                                onHandStateChange = { isLeft, handState ->
                                    viewModel.setHandState(isLeft, handState)
                                },
                                onViewPromptClick = { viewModel.selectTab(2) }
                            )
                            1 -> DiagnosticsScreen(
                                uiState = uiState,
                                onToggleAutoCorrect = { viewModel.toggleAutoCorrect() }
                            )
                            2 -> CompiledPromptScreen(
                                uiState = uiState,
                                onModelChange = { viewModel.setTargetModel(it) },
                                onRandomizeSeed = { viewModel.randomizeSeed() },
                                onSavePrompt = { viewModel.saveCurrentPrompt() }
                            )
                            3 -> HistoryScreen(
                                savedPrompts = savedPrompts,
                                onLoadPreset = { viewModel.loadPreset(it) },
                                onDeletePrompt = { viewModel.deletePrompt(it) },
                                onToggleFavorite = { id, currentFav ->
                                    viewModel.toggleFavorite(id, currentFav)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class NavItem(
    val labelEn: String,
    val labelAr: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)
