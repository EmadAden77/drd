package com.example.ui.screens

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.EngineUiState

private data class BuilderCategory(
    val titleEn: String,
    val titleAr: String,
    val icon: ImageVector
)

private val categories = listOf(
    BuilderCategory("Capture & Scene", "الالتقاط والمشهد", Icons.Default.CameraAlt),
    BuilderCategory("Subject & Identity", "الهوية والشخصية", Icons.Default.Person),
    BuilderCategory("Pose & Surface", "الوضعية والسطح", Icons.Default.AccessibilityNew),
    BuilderCategory("Camera 3D", "أبعاد الكاميرا", Icons.Default.Tune),
    BuilderCategory("Hand Budget", "ميزانية الأيدي", Icons.Default.PanTool),
    BuilderCategory("Lighting Physics", "مسار الإضاءة", Icons.Default.WbSunny),
    BuilderCategory("Phone Camera", "سلوك الهاتف", Icons.Default.PhoneAndroid)
)

@Composable
fun BuilderScreen(
    uiState: EngineUiState,
    onCategorySelect: (Int) -> Unit,
    onCaptureTypeSelect: (CaptureType) -> Unit,
    onSceneContextSelect: (SceneContext) -> Unit,
    onStateUpdate: ((SceneState) -> SceneState) -> Unit,
    onHandStateChange: (Boolean, HandState) -> Unit,
    onViewPromptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = uiState.sceneState
    val currentCat = uiState.builderCategoryIndex

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SpaceBlack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onViewPromptClick,
                containerColor = ElectricSky,
                contentColor = SpaceBlack,
                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                text = {
                    Column {
                        Text("Compile Prompt", fontWeight = FontWeight.Bold)
                        Text("عرض الـPrompt النهائي", fontSize = 10.sp)
                    }
                },
                modifier = Modifier
                    .testTag("compile_prompt_fab")
                    .padding(bottom = 16.dp)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Horizontal Categories Scroll Tab Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEachIndexed { index, cat ->
                        val isSelected = index == currentCat
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onCategorySelect(index) }
                                .minimumInteractiveComponentSize(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) ElectricSky else SurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) ElectricSky else BorderSubtle
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) SpaceBlack else TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = cat.titleEn,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = if (isSelected) SpaceBlack else TextPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                    Text(
                                        text = cat.titleAr,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) SpaceBlack.copy(alpha = 0.8f) else AmberGlow,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Category Specific Content
            when (currentCat) {
                0 -> {
                    // Category 0: Capture & Scene
                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Capture Mode & Rig Architecture",
                                titleAr = "نوع الالتقاط وهندسة الحامل",
                                icon = Icons.Default.CameraAlt
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            CaptureType.values().forEach { type ->
                                SelectableOptionPill(
                                    item = type,
                                    isSelected = state.captureType == type,
                                    titleEn = type.labelEn,
                                    titleAr = type.labelAr,
                                    subtitle = type.description,
                                    onClick = { onCaptureTypeSelect(type) },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Physical Scene Environment",
                                titleAr = "موقع وبيئة المشهد الواقعي",
                                icon = Icons.Default.Place
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            SceneContext.values().forEach { ctx ->
                                SelectableOptionPill(
                                    item = ctx,
                                    isSelected = state.sceneContext == ctx,
                                    titleEn = ctx.labelEn,
                                    titleAr = ctx.labelAr,
                                    subtitle = "Category: ${ctx.category}",
                                    onClick = { onSceneContextSelect(ctx) },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Time of Day & Sun Elevation",
                                titleAr = "وقت التصوير وارتفاع الشمس",
                                icon = Icons.Default.Schedule
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            TimeOfDay.values().forEach { time ->
                                SelectableOptionPill(
                                    item = time,
                                    isSelected = state.timeOfDay == time,
                                    titleEn = time.labelEn,
                                    titleAr = time.labelAr,
                                    subtitle = "Standard Kelvin: ${time.kelvinDefault}K",
                                    onClick = {
                                        onStateUpdate { s ->
                                            s.copy(timeOfDay = time, colorTempK = time.kelvinDefault)
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // Category 1: Subject & Identity
                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Identity & Facial Features",
                                titleAr = "هوية الوجه والملامح (مستقلة عن الوضعية)",
                                icon = Icons.Default.Person
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = state.identityAnchor,
                                onValueChange = { newVal ->
                                    onStateUpdate { s -> s.copy(identityAnchor = newVal) }
                                },
                                label = { Text("Identity Anchor (Reference Guidance)") },
                                placeholder = { Text("Describe consistent ethnic and facial features...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("identity_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ElectricSky,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Gender buttons
                                Gender.values().forEach { g ->
                                    val isSelected = state.gender == g
                                    Button(
                                        onClick = { onStateUpdate { s -> s.copy(gender = g) } },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) ElectricSky else SurfaceCardHighlight,
                                            contentColor = if (isSelected) SpaceBlack else TextPrimary
                                        )
                                    ) {
                                        Text("${g.labelEn} (${g.labelAr})")
                                    }
                                }
                            }
                        }
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Hair Structure & Headdress Mechanics",
                                titleAr = "بنية الشعر والغطاء والرأس",
                                icon = Icons.Default.Face
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            HairStyle.values().forEach { h ->
                                SelectableOptionPill(
                                    item = h,
                                    isSelected = state.hairStyle == h,
                                    titleEn = h.labelEn,
                                    titleAr = h.labelAr,
                                    onClick = { onStateUpdate { s -> s.copy(hairStyle = h) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Authentic Clothing & Textiles",
                                titleAr = "الملابس والأقمشة الواقعية",
                                icon = Icons.Default.Checkroom
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            ClothingType.values().forEach { c ->
                                SelectableOptionPill(
                                    item = c,
                                    isSelected = state.clothingType == c,
                                    titleEn = c.labelEn,
                                    titleAr = c.labelAr,
                                    onClick = { onStateUpdate { s -> s.copy(clothingType = c) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Expression & Micro-Emotion",
                                titleAr = "تعبير الوجه والانفعال العفوي",
                                icon = Icons.Default.Mood
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Expression.values().forEach { exp ->
                                SelectableOptionPill(
                                    item = exp,
                                    isSelected = state.expression == exp,
                                    titleEn = exp.labelEn,
                                    titleAr = exp.labelAr,
                                    onClick = { onStateUpdate { s -> s.copy(expression = exp) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                2 -> {
                    // Category 2: Pose & Surface
                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Body Pose & Biomechanics",
                                titleAr = "وضعية الجسم وتوزيع الثقل",
                                icon = Icons.Default.AccessibilityNew
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            BodyPose.values().forEach { p ->
                                SelectableOptionPill(
                                    item = p,
                                    isSelected = state.bodyPose == p,
                                    titleEn = p.labelEn,
                                    titleAr = p.labelAr,
                                    subtitle = "Compatible surfaces: ${p.applicableSurfaces.joinToString { it.labelEn }}",
                                    onClick = { onStateUpdate { s -> s.copy(bodyPose = p) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Support Surface & Compression",
                                titleAr = "سطح الارتكاز وانضغاط الوزن",
                                icon = Icons.Default.Chair
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            SupportSurface.values().forEach { surf ->
                                SelectableOptionPill(
                                    item = surf,
                                    isSelected = state.supportSurface == surf,
                                    titleEn = surf.labelEn,
                                    titleAr = surf.labelAr,
                                    subtitle = "Compression: ${surf.compressionLevel}",
                                    onClick = { onStateUpdate { s -> s.copy(supportSurface = surf) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // Category 3: Camera 3D Geometry
                    item {
                        CameraGeometryWidget(
                            captureType = state.captureType,
                            cameraAngle = state.cameraAngle,
                            cameraDistance = state.cameraDistance,
                            lensType = state.lensType,
                            pitchDeg = state.pitchDeg,
                            yawDeg = state.yawDeg,
                            onPitchChange = { p -> onStateUpdate { s -> s.copy(pitchDeg = p) } },
                            onYawChange = { y -> onStateUpdate { s -> s.copy(yawDeg = y) } },
                            onAngleChange = { a -> onStateUpdate { s -> s.copy(cameraAngle = a) } },
                            onDistanceChange = { d -> onStateUpdate { s -> s.copy(cameraDistance = d) } },
                            onLensChange = { l -> onStateUpdate { s -> s.copy(lensType = l) } }
                        )
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Lens Simulation (Focal Length)",
                                titleAr = "نوع العدسة والبعد البؤري",
                                icon = Icons.Default.PhotoCamera
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LensType.values().forEach { lens ->
                                SelectableOptionPill(
                                    item = lens,
                                    isSelected = state.lensType == lens,
                                    titleEn = lens.labelEn,
                                    titleAr = lens.labelAr,
                                    subtitle = "Focal Length: ${lens.focalLengthMm}mm",
                                    onClick = { onStateUpdate { s -> s.copy(lensType = lens) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                4 -> {
                    // Category 4: Hand Budget
                    item {
                        HandBudgetVisualizer(
                            leftHand = state.leftHand,
                            rightHand = state.rightHand,
                            onLeftHandChange = { h -> onHandStateChange(true, h) },
                            onRightHandChange = { h -> onHandStateChange(false, h) }
                        )
                    }
                }

                5 -> {
                    // Category 5: Lighting Physics
                    item {
                        LightingDiagramWidget(
                            lightingSource = state.lightingSource,
                            lightDirection = state.lightDirection,
                            colorTempK = state.colorTempK,
                            shadowHardness = state.shadowHardnessPercent,
                            onSourceChange = { src -> onStateUpdate { s -> s.copy(lightingSource = src) } },
                            onDirectionChange = { dir -> onStateUpdate { s -> s.copy(lightDirection = dir) } },
                            onTempChange = { t -> onStateUpdate { s -> s.copy(colorTempK = t) } },
                            onShadowHardnessChange = { h -> onStateUpdate { s -> s.copy(shadowHardnessPercent = h) } }
                        )
                    }

                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Key Light Vector Direction",
                                titleAr = "اتجاه مصدر الضوء الرئيسي",
                                icon = Icons.Default.NorthEast
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LightDirection.values().forEach { dir ->
                                SelectableOptionPill(
                                    item = dir,
                                    isSelected = state.lightDirection == dir,
                                    titleEn = dir.labelEn,
                                    titleAr = dir.labelAr,
                                    onClick = { onStateUpdate { s -> s.copy(lightDirection = dir) } },
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                6 -> {
                    // Category 6: Smartphone Imperfections
                    item {
                        StudioCard {
                            SectionHeader(
                                titleEn = "Smartphone Camera Imperfections",
                                titleAr = "خصائص ومحاكاة كاميرا الهواتف الحقيقية",
                                icon = Icons.Default.PhoneAndroid
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Real mobile photos are never sterile 3D renders. Enabling optical flaws forces the image generator to produce believable textures and avoid synthetic AI plastic sheen.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            OpticToggleRow(
                                title = "Shadow Sensor Noise (Grain)",
                                subtitle = "تحبب ناعم في مناطق الظلال لتفادي التنعيم الرقمي",
                                isChecked = state.enableSensorNoise,
                                onCheckedChange = { ch -> onStateUpdate { s -> s.copy(enableSensorNoise = ch) } }
                            )

                            OpticToggleRow(
                                title = "Edge Softness & Vignetting",
                                subtitle = "نعومة طبيعية خفيفة عند حواف العدسة",
                                isChecked = state.enableEdgeSoftness,
                                onCheckedChange = { ch -> onStateUpdate { s -> s.copy(enableEdgeSoftness = ch) } }
                            )

                            OpticToggleRow(
                                title = "Mobile Wide-Angle Barrel Distortion",
                                subtitle = "انحناء أطراف العدسة الواسعة للهاتف (24-26 مم)",
                                isChecked = state.enableSubtleBarrelDistortion,
                                onCheckedChange = { ch -> onStateUpdate { s -> s.copy(enableSubtleBarrelDistortion = ch) } }
                            )

                            OpticToggleRow(
                                title = "Window Highlight Overexposure",
                                subtitle = "حرق خفيف للضوء في النوافذ (نطاق ديناميكي حقيقي)",
                                isChecked = state.enableWindowHighlightBlowout,
                                onCheckedChange = { ch -> onStateUpdate { s -> s.copy(enableWindowHighlightBlowout = ch) } }
                            )

                            OpticToggleRow(
                                title = "Moderate Mobile Computational HDR",
                                subtitle = "معالجة HDR طبيعية دون تلوين مفرط",
                                isChecked = state.enableModerateHDR,
                                onCheckedChange = { ch -> onStateUpdate { s -> s.copy(enableModerateHDR = ch) } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OpticToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp
                )
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SpaceBlack,
                checkedTrackColor = ElectricSky,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = SurfaceCardHighlight
            )
        )
    }
}
