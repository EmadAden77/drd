package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HandState
import com.example.ui.theme.*

@Composable
fun HandBudgetVisualizer(
    leftHand: HandState,
    rightHand: HandState,
    onLeftHandChange: (HandState) -> Unit,
    onRightHandChange: (HandState) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalOccupied = (if (leftHand != HandState.EMPTY_RELAXED) 1 else 0) +
            (if (rightHand != HandState.EMPTY_RELAXED) 1 else 0)

    StudioCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hand Budget & Biomechanics",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "ميزانية الأيدي ومنع اليد الثالثة (الحد الأقصى: يدان)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AmberGlow,
                        fontSize = 11.sp
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (totalOccupied <= 2) EmeraldCoherence.copy(alpha = 0.2f) else CoralViolation.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (totalOccupied <= 2) EmeraldCoherence else CoralViolation
                )
            ) {
                Text(
                    text = "$totalOccupied / 2 Hands",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (totalOccupied <= 2) EmeraldCoherence else CoralViolation,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Hand Card
            HandSlotCard(
                handNameEn = "Left Hand",
                handNameAr = "اليد اليسرى",
                current = leftHand,
                onSelect = onLeftHandChange,
                modifier = Modifier.weight(1f)
            )

            // Right Hand Card
            HandSlotCard(
                handNameEn = "Right Hand",
                handNameAr = "اليد اليمنى",
                current = rightHand,
                onSelect = onRightHandChange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Biological constraint notice
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SpaceBlack.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ElectricSky,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "If a hand holds the smartphone, it is locked from holding props simultaneously.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
fun HandSlotCard(
    handNameEn: String,
    handNameAr: String,
    current: HandState,
    onSelect: (HandState) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val icon: ImageVector = when {
        current.isHoldingPhone -> Icons.Default.PhoneAndroid
        current.isHoldingItem -> Icons.Default.LocalCafe
        else -> Icons.Default.PanTool
    }

    val iconColor = when {
        current.isHoldingPhone -> ElectricSky
        current.isHoldingItem -> AmberGlow
        else -> TextSecondary
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .clickable { expanded = true }
            .minimumInteractiveComponentSize(),
        color = SurfaceCardHighlight,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = handNameEn,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = handNameAr,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AmberGlow,
                    fontSize = 10.sp
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SpaceBlack.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = current.labelEn,
                    modifier = Modifier.padding(6.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = iconColor,
                        fontSize = 11.sp
                    ),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap to change",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 10.sp
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(SurfaceCard)
            ) {
                HandState.values().forEach { state ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = state.labelEn,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (state == current) ElectricSky else TextPrimary,
                                        fontWeight = if (state == current) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                                Text(
                                    text = state.labelAr,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AmberGlow,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        },
                        onClick = {
                            onSelect(state)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
