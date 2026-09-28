package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.db.PromptEntity
import com.example.engine.ScenePreset
import com.example.engine.ScenePresets
import com.example.ui.components.SectionHeader
import com.example.ui.components.StudioCard
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    savedPrompts: List<PromptEntity>,
    onLoadPreset: (ScenePreset) -> Unit,
    onDeletePrompt: (Long) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedSubTab by remember { mutableStateOf(0) } // 0: Presets, 1: Saved Prompts

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tab switcher
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .padding(4.dp)
            ) {
                // Presets tab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { selectedSubTab = 0 }
                        .minimumInteractiveComponentSize(),
                    color = if (selectedSubTab == 0) ElectricSky else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Realistic Presets",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedSubTab == 0) SpaceBlack else TextPrimary
                            )
                        )
                        Text(
                            text = "نماذج واقعية جاهزة",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (selectedSubTab == 0) SpaceBlack.copy(alpha = 0.8f) else AmberGlow,
                                fontSize = 9.sp
                            )
                        )
                    }
                }

                // Saved Library tab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { selectedSubTab = 1 }
                        .minimumInteractiveComponentSize(),
                    color = if (selectedSubTab == 1) ElectricSky else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Saved History (${savedPrompts.size})",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedSubTab == 1) SpaceBlack else TextPrimary
                            )
                        )
                        Text(
                            text = "المحفوظات الشخصية",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (selectedSubTab == 1) SpaceBlack.copy(alpha = 0.8f) else AmberGlow,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }

        if (selectedSubTab == 0) {
            // Presets List
            items(ScenePresets.presets) { preset ->
                StudioCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = preset.titleEn,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = preset.titleAr,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AmberGlow,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Button(
                            onClick = {
                                onLoadPreset(preset)
                                Toast.makeText(context, "Loaded preset: ${preset.titleEn}", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricSky,
                                contentColor = SpaceBlack
                            )
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Load")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = preset.descriptionEn,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 12.sp)
                    )
                    Text(
                        text = preset.descriptionAr,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SurfaceCardHighlight
                        ) {
                            Text(
                                text = preset.state.captureType.labelEn,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = ElectricSky, fontSize = 10.sp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SurfaceCardHighlight
                        ) {
                            Text(
                                text = "${preset.state.lensType.focalLengthMm}mm",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = AmberGlow, fontSize = 10.sp)
                            )
                        }
                    }
                }
            }
        } else {
            // Saved prompts from Room DB
            if (savedPrompts.isEmpty()) {
                item {
                    StudioCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Saved Prompts Yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "لم تقم بحفظ أي Prompt حتى الآن. اضغط على أيقونة الحفظ في شاشة الـPrompt لحفظه هنا.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            } else {
                items(savedPrompts) { item ->
                    val sdf = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
                    StudioCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "${item.targetModel} • Seed: ${item.seed} • ${sdf.format(Date(item.timestamp))}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AmberGlow,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Saved Prompt", item.fullPrompt)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Copied saved prompt! ✓", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = ElectricSky)
                                }

                                IconButton(
                                    onClick = { onDeletePrompt(item.id) }
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = CoralViolation)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SpaceBlack.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.fullPrompt,
                                modifier = Modifier.padding(10.dp),
                                maxLines = 3,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
