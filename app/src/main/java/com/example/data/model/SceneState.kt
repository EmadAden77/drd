package com.example.data.model

/**
 * Immutable representation of the physical scene parameters.
 */
data class SceneState(
    val captureType: CaptureType = CaptureType.SUBJECT_HELD_SELFIE,
    val sceneContext: SceneContext = SceneContext.CAFE_CONTEMPORARY,
    val culturalFidelity: CulturalFidelity = CulturalFidelity.SAUDI_AUTHENTIC,
    val timeOfDay: TimeOfDay = TimeOfDay.GOLDEN_HOUR,

    // Subject Identity & Demographics
    val gender: Gender = Gender.MALE,
    val ageGroup: AgeGroup = AgeGroup.LATE_20S,
    val identityAnchor: String = "Saudi man with authentic facial features, slight natural 3-day stubble, almond brown eyes",
    val expression: Expression = Expression.NEUTRAL_SUBTLE_CONFIDENT,
    val hairStyle: HairStyle = HairStyle.MODERN_TAPERED_SHORT,
    val clothingType: ClothingType = ClothingType.WHITE_COTTON_THOBE,

    // Pose & Surface Mechanics
    val bodyPose: BodyPose = BodyPose.SITTING_CHAIR_RELAXED,
    val supportSurface: SupportSurface = SupportSurface.WOODEN_CHAIR,

    // Camera Rig & Geometry
    val cameraAngle: CameraAngle = CameraAngle.SLIGHT_HIGH_ANGLE,
    val cameraDistance: CameraDistance = CameraDistance.ARMS_LENGTH,
    val lensType: LensType = LensType.SMARTPHONE_26MM_MAIN,
    val pitchDeg: Int = 15,
    val yawDeg: Int = -5,
    val rollDeg: Int = 0,

    // Hand Budget
    val leftHand: HandState = HandState.RESTING_ON_TABLE,
    val rightHand: HandState = HandState.HOLDING_PHONE,

    // Lighting Causality
    val lightingSource: LightingSource = LightingSource.CAFE_PENDANT_LAMPS,
    val lightDirection: LightDirection = LightDirection.FRONT_45_KEY,
    val colorTempK: Int = 2900,
    val shadowHardnessPercent: Int = 45,

    // Smartphone Optical Imperfections
    val enableSensorNoise: Boolean = true,
    val enableEdgeSoftness: Boolean = true,
    val enableSubtleBarrelDistortion: Boolean = true,
    val enableWindowHighlightBlowout: Boolean = true,
    val enableModerateHDR: Boolean = true,

    // Compilation Target & Determinism
    val targetModel: TargetModel = TargetModel.CHATGPT_DALL_E_3,
    val seed: Long = 4829104L,
    val customNotes: String = ""
) {
    /**
     * Total hands dedicated to camera phone.
     */
    val phoneHandsCount: Int
        get() {
            var count = 0
            if (leftHand.isHoldingPhone) count++
            if (rightHand.isHoldingPhone) count++
            return count
        }

    /**
     * Total hands dedicated to props/cups/items.
     */
    val itemHandsCount: Int
        get() {
            var count = 0
            if (leftHand.isHoldingItem) count++
            if (rightHand.isHoldingItem) count++
            return count
        }
}
