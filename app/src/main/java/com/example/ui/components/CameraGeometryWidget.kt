package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun CameraGeometryWidget(
    captureType: CaptureType,
    cameraAngle: CameraAngle,
    cameraDistance: CameraDistance,
    lensType: LensType,
    pitchDeg: Int,
    yawDeg: Int,
    onPitchChange: (Int) -> Unit,
    onYawChange: (Int) -> Unit,
    onAngleChange: (CameraAngle) -> Unit,
    onDistanceChange: (CameraDistance) -> Unit,
    onLensChange: (LensType) -> Unit,
    modifier: Modifier = Modifier
) {
    StudioCard(modifier = modifier) {
        SectionHeader(
            titleEn = "Camera Geometry & 3D Optics",
            titleAr = "هندسة الكاميرا وزوايا التصوير ثلاثية الأبعاد",
            icon = Icons.Default.CameraAlt,
            badgeText = "${lensType.focalLengthMm}mm • ${cameraDistance.meters}m"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Distance & Reach indicator
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SpaceBlack.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Subject-Camera Distance",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "${cameraDistance.labelEn} (${cameraDistance.meters}m)",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ElectricSky,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (captureType == CaptureType.SUBJECT_HELD_SELFIE && cameraDistance.meters <= 0.8f) {
                        EmeraldCoherence.copy(alpha = 0.2f)
                    } else if (captureType == CaptureType.SUBJECT_HELD_SELFIE) {
                        CoralViolation.copy(alpha = 0.2f)
                    } else {
                        ElectricSky.copy(alpha = 0.2f)
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (captureType == CaptureType.SUBJECT_HELD_SELFIE && cameraDistance.meters <= 0.8f) {
                            EmeraldCoherence
                        } else if (captureType == CaptureType.SUBJECT_HELD_SELFIE) {
                            CoralViolation
                        } else {
                            ElectricSky
                        }
                    )
                ) {
                    Text(
                        text = if (captureType == CaptureType.SUBJECT_HELD_SELFIE) "Arm Limit ≤ 0.8m" else "External Rig",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextPrimary,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pitch Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pitch Elevation: ${pitchDeg}° (${if (pitchDeg > 0) "Downward / High" else if (pitchDeg < 0) "Upward / Low" else "Neutral"})",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Text(
                    text = "زاوية الميل الرأسي",
                    style = MaterialTheme.typography.labelSmall.copy(color = AmberGlow, fontSize = 10.sp)
                )
            }
            Slider(
                value = pitchDeg.toFloat(),
                onValueChange = { onPitchChange(it.toInt()) },
                valueRange = -25f..30f,
                steps = 10,
                colors = SliderDefaults.colors(
                    thumbColor = ElectricSky,
                    activeTrackColor = ElectricSky,
                    inactiveTrackColor = BorderSubtle
                )
            )
        }

        // Yaw Slider
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Yaw Rotation: ${yawDeg}° (${if (yawDeg > 0) "Right" else if (yawDeg < 0) "Left" else "Direct"})",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Text(
                    text = "دوران الكاميرا الجانبي",
                    style = MaterialTheme.typography.labelSmall.copy(color = AmberGlow, fontSize = 10.sp)
                )
            }
            Slider(
                value = yawDeg.toFloat(),
                onValueChange = { onYawChange(it.toInt()) },
                valueRange = -30f..30f,
                steps = 11,
                colors = SliderDefaults.colors(
                    thumbColor = AmberGlow,
                    activeTrackColor = AmberGlow,
                    inactiveTrackColor = BorderSubtle
                )
            )
        }
    }
}
