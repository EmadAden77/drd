package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LightDirection
import com.example.data.model.LightingSource
import com.example.ui.theme.*

@Composable
fun LightingDiagramWidget(
    lightingSource: LightingSource,
    lightDirection: LightDirection,
    colorTempK: Int,
    shadowHardness: Int,
    onSourceChange: (LightingSource) -> Unit,
    onDirectionChange: (LightDirection) -> Unit,
    onTempChange: (Int) -> Unit,
    onShadowHardnessChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate preview color based on Kelvin
    val kelvinColor = when {
        colorTempK <= 2600 -> Color(0xFFFF9E44) // Very warm sodium
        colorTempK <= 3400 -> Color(0xFFFFC078) // Golden hour / tungsten
        colorTempK <= 4500 -> Color(0xFFFFE8D1) // Warm white
        colorTempK <= 5800 -> Color(0xFFFFFFFF) // Midday daylight
        colorTempK <= 6800 -> Color(0xFFD6E4FF) // Overcast daylight
        else -> Color(0xFFADC8FF) // Blue hour sky
    }

    StudioCard(modifier = modifier) {
        SectionHeader(
            titleEn = "Lighting Causality & Photometrics",
            titleAr = "فيزياء وسببية مسار الضوء والظلال",
            icon = Icons.Default.WbSunny,
            badgeText = "$colorTempK K"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Kelvin & Source Banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SpaceBlack.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(kelvinColor)
                        .border(2.dp, BorderSubtle, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lightingSource.labelEn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${lightDirection.labelEn} • $colorTempK Kelvin",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Kelvin Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Color Temperature: $colorTempK K (${getKelvinDescription(colorTempK)})",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Text(
                    text = "حرارة اللون (كلفن)",
                    style = MaterialTheme.typography.labelSmall.copy(color = AmberGlow, fontSize = 10.sp)
                )
            }
            Slider(
                value = colorTempK.toFloat(),
                onValueChange = { onTempChange(it.toInt()) },
                valueRange = 2200f..7500f,
                steps = 26,
                colors = SliderDefaults.colors(
                    thumbColor = AmberGlow,
                    activeTrackColor = AmberGlow,
                    inactiveTrackColor = BorderSubtle
                )
            )
        }

        // Shadow Hardness Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shadow Penumbra Hardness: $shadowHardness%",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Text(
                    text = "حدة الظل ونصف الظل",
                    style = MaterialTheme.typography.labelSmall.copy(color = AmberGlow, fontSize = 10.sp)
                )
            }
            Slider(
                value = shadowHardness.toFloat(),
                onValueChange = { onShadowHardnessChange(it.toInt()) },
                valueRange = 10f..90f,
                steps = 15,
                colors = SliderDefaults.colors(
                    thumbColor = ElectricSky,
                    activeTrackColor = ElectricSky,
                    inactiveTrackColor = BorderSubtle
                )
            )
        }
    }
}

private fun getKelvinDescription(kelvin: Int): String {
    return when {
        kelvin <= 2500 -> "Warm Sodium / Candle"
        kelvin <= 3300 -> "Golden Sunset"
        kelvin <= 4500 -> "Halogen / Warm White"
        kelvin <= 5600 -> "Direct Sun"
        kelvin <= 6500 -> "Overcast Sky"
        else -> "Deep Blue Hour"
    }
}
