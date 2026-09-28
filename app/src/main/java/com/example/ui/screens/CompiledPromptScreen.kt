package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TargetModel
import com.example.engine.PromptSection
import com.example.ui.components.SectionHeader
import com.example.ui.components.StudioCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.EngineUiState

@Composable
fun CompiledPromptScreen(
    uiState: EngineUiState,
    onModelChange: (TargetModel) -> Unit,
    onRandomizeSeed: () -> Unit,
    onSavePrompt: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val compiled = uiState.compiledPrompt
    val state = uiState.sceneState
    var showSectionsBreakdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Target Engine Selector
        item {
            StudioCard {
                SectionHeader(
                    titleEn = "Target Image Generation Model",
                    titleAr = "نموذج الذكاء الاصطناعي المستهدف",
                    icon = Icons.Default.Tune
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TargetModel.values().forEach { model ->
                        val isSelected = state.targetModel == model
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onModelChange(model) }
                                .minimumInteractiveComponentSize(),
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) ElectricSky else SurfaceCardHighlight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) ElectricSky else BorderSubtle
                            )
                        ) {
                            Text(
                                text = model.labelEn,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) SpaceBlack else TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }

        // Deterministic Seed Card
        item {
            StudioCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Deterministic Seed Engine",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "تثبيت الـSeed لضمان إعادة إنتاج نفس البنية بدقة",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AmberGlow,
                                fontSize = 11.sp
                            )
                        )
                    }

                    FilledTonalButton(
                        onClick = onRandomizeSeed,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = ElectricSky.copy(alpha = 0.15f),
                            contentColor = ElectricSky
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Randomize")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SpaceBlack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Seed: ${state.seed}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = ElectricSky,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Fixed Determinism",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }
            }
        }

        // Full Compiled Prompt Output
        item {
            StudioCard(
                borderColor = ElectricSky.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Compiled Coherent Prompt",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "${compiled.targetModel.labelEn} • 10 Structured Sections",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ElectricSky,
                                fontSize = 11.sp
                            )
                        )
                    }

                    // Action buttons: Copy & Save
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Compiled Prompt", compiled.fullPrompt)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied prompt to clipboard! ✓", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .testTag("copy_prompt_button")
                                .background(ElectricSky.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = ElectricSky)
                        }

                        IconButton(
                            onClick = {
                                onSavePrompt()
                                Toast.makeText(context, "Saved to local library! ✓", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .testTag("save_prompt_button")
                                .background(AmberGlow.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = "Save", tint = AmberGlow)
                        }

                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, compiled.fullPrompt)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Prompt"))
                            },
                            modifier = Modifier.background(SurfaceCardHighlight, RoundedCornerShape(10.dp))
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = TextPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // The Prompt Text Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SpaceBlack.copy(alpha = 0.85f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = compiled.fullPrompt,
                        modifier = Modifier
                            .padding(14.dp)
                            .testTag("compiled_prompt_text"),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextPrimary,
                            lineHeight = 20.sp,
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle breakdown
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showSectionsBreakdown = !showSectionsBreakdown }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showSectionsBreakdown) "Hide Section Breakdown" else "View 10 Modular Sections",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ElectricSky,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Icon(
                        imageVector = if (showSectionsBreakdown) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = ElectricSky
                    )
                }
            }
        }

        // Section Breakdown List (if expanded)
        if (showSectionsBreakdown) {
            items(compiled.sections) { sec ->
                SectionBreakdownCard(section = sec)
            }
        }
    }
}

@Composable
private fun SectionBreakdownCard(section: PromptSection) {
    StudioCard(backgroundColor = DeepNavySurface) {
        Text(
            text = section.titleEn,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = ElectricSky
            )
        )
        Text(
            text = section.titleAr,
            style = MaterialTheme.typography.bodySmall.copy(
                color = AmberGlow,
                fontSize = 11.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = section.content,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                lineHeight = 18.sp,
                fontSize = 11.sp
            )
        )
    }
}
