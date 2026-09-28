package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RuleStatus
import com.example.ui.theme.*

@Composable
fun StudioCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SurfaceCard,
    borderColor: Color = BorderSubtle,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun SectionHeader(
    titleEn: String,
    titleAr: String,
    icon: ImageVector? = null,
    badgeText: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ElectricSky.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ElectricSky,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titleEn,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = titleAr,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AmberGlow,
                    fontSize = 11.sp
                )
            )
        }
        if (badgeText != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ElectricSkyDark.copy(alpha = 0.25f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricSky.copy(alpha = 0.4f))
            ) {
                Text(
                    text = badgeText,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricSky,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun CoherenceStatusBadge(
    violations: Int,
    autoCorrections: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon, label) = when {
        violations > 0 -> Quad(
            CoralViolation.copy(alpha = 0.2f),
            CoralViolation,
            Icons.Default.Error,
            "$violations Violation${if (violations > 1) "s" else ""}"
        )
        autoCorrections > 0 -> Quad(
            AmberGlow.copy(alpha = 0.2f),
            AmberGlow,
            Icons.Default.Warning,
            "$autoCorrections Auto-Corrected"
        )
        else -> Quad(
            EmeraldCoherence.copy(alpha = 0.2f),
            EmeraldCoherence,
            Icons.Default.CheckCircle,
            "Physics Coherent ✓"
        )
    }

    Surface(
        modifier = modifier
            .testTag("coherence_badge")
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            )
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun <T> SelectableOptionPill(
    item: T,
    isSelected: Boolean,
    titleEn: String,
    titleAr: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isSelected) ElectricSky.copy(alpha = 0.15f) else SurfaceCardHighlight.copy(alpha = 0.5f)
    val border = if (isSelected) ElectricSky else BorderSubtle

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .minimumInteractiveComponentSize(),
        color = bg,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) ElectricSky else Color.Transparent)
                    .border(2.dp, if (isSelected) ElectricSky else TextMuted, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(SpaceBlack)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titleEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TextPrimary else TextSecondary
                    )
                )
                Text(
                    text = titleAr,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSelected) AmberGlow else TextMuted,
                        fontSize = 11.sp
                    )
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}
