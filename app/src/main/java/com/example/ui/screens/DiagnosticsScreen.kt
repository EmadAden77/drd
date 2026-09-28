package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiagnosticRule
import com.example.data.model.RuleStatus
import com.example.ui.components.SectionHeader
import com.example.ui.components.StudioCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.EngineUiState

@Composable
fun DiagnosticsScreen(
    uiState: EngineUiState,
    onToggleAutoCorrect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val eval = uiState.evaluationResult

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header summary banner
        item {
            StudioCard(
                borderColor = if (eval.violationsCount > 0) CoralViolation else if (eval.autoCorrectionsCount > 0) AmberGlow else EmeraldCoherence
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (eval.violationsCount > 0) CoralViolation.copy(alpha = 0.2f)
                                else if (eval.autoCorrectionsCount > 0) AmberGlow.copy(alpha = 0.2f)
                                else EmeraldCoherence.copy(alpha = 0.2f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (eval.violationsCount > 0) Icons.Default.Error
                            else if (eval.autoCorrectionsCount > 0) Icons.Default.Warning
                            else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (eval.violationsCount > 0) CoralViolation
                            else if (eval.autoCorrectionsCount > 0) AmberGlow
                            else EmeraldCoherence,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (eval.violationsCount > 0) "Physics Inconsistencies Detected"
                            else if (eval.autoCorrectionsCount > 0) "Coherence Auto-Resolved"
                            else "100% Physical Coherence Verified",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "${eval.rules.size} physical laws checked • ${eval.autoCorrectionsCount} resolved",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AmberGlow,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Auto-Correction Switch Row
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SpaceBlack.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Autonomous State Resolution",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "يمنع الـInvalid State فوراً ويعيد ضبط التعارضات آلياً",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Switch(
                            checked = uiState.autoCorrectEnabled,
                            onCheckedChange = { onToggleAutoCorrect() },
                            modifier = Modifier.testTag("auto_correct_switch"),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SpaceBlack,
                                checkedTrackColor = ElectricSky
                            )
                        )
                    }
                }
            }
        }

        // Philosophy Callout
        item {
            StudioCard(backgroundColor = DeepNavySurface) {
                Text(
                    text = "Constraint Engine Architecture",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = ElectricSky,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Structured Scene → Rules → Compatibility → Physics → Prompt",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "الهدف الأساسي هو جعل المشهد نفسه صحيحاً وممكناً فيزيائياً في العالم الحقيقي قبل صياغة الـPrompt. فالنماذج تخطئ عندما يحتوي الوصف على أيدٍ زائدة أو مسافات كاميرا مستحيلة أو إضاءة بلا مصدر حقيقي.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        // List of Rules
        items(eval.rules) { rule ->
            RuleItemCard(rule = rule)
        }
    }
}

@Composable
private fun RuleItemCard(rule: DiagnosticRule) {
    val (statusColor, statusIcon, statusLabel) = when (rule.status) {
        RuleStatus.COHERENT -> Triple(EmeraldCoherence, Icons.Default.Check, "Coherent ✓")
        RuleStatus.WARNING_AUTO_CORRECTED -> Triple(AmberGlow, Icons.Default.Sync, "Auto-Resolved ⚡")
        RuleStatus.PHYSICS_VIOLATION -> Triple(CoralViolation, Icons.Default.Close, "Violation ✗")
    }

    StudioCard(
        borderColor = statusColor.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rule.titleEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = rule.titleAr,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AmberGlow,
                        fontSize = 11.sp
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = statusColor.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = rule.explanationEn,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 12.sp
            )
        )
        Text(
            text = rule.explanationAr,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextMuted,
                fontSize = 11.sp
            )
        )

        if (rule.correctionApplied != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AmberGlowDark.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = AmberGlow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = rule.correctionApplied,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AmberGlow,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
