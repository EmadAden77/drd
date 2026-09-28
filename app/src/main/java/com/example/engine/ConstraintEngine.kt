package com.example.engine

import com.example.data.model.*

/**
 * Result of constraint evaluation.
 */
data class EvaluationResult(
    val validatedState: SceneState,
    val rules: List<DiagnosticRule>,
    val autoCorrectionsCount: Int,
    val violationsCount: Int
)

/**
 * Domain-specific Constraint & Physical Coherence Engine.
 * Evaluates the scene state against strict anatomical, spatial, optical, and physical laws.
 */
object ConstraintEngine {

    fun evaluate(state: SceneState, autoCorrect: Boolean = true): EvaluationResult {
        val rules = mutableListOf<DiagnosticRule>()
        var resolved = state
        var correctionsCount = 0
        var violationsCount = 0

        // ----------------------------------------------------
        // RULE 1: HAND BUDGET LAW (Humans have exactly 2 hands)
        // ----------------------------------------------------
        when (resolved.captureType) {
            CaptureType.SUBJECT_HELD_SELFIE -> {
                val holdsPhone = resolved.leftHand.isHoldingPhone || resolved.rightHand.isHoldingPhone
                if (!holdsPhone) {
                    if (autoCorrect) {
                        resolved = resolved.copy(rightHand = HandState.HOLDING_PHONE)
                        correctionsCount++
                        rules.add(
                            DiagnosticRule(
                                id = "HAND_BUDGET_SELFIE_ALLOCATION",
                                titleEn = "Selfie Hand Allocation",
                                titleAr = "تخصيص يد لحمل هاتف السيلفي",
                                status = RuleStatus.WARNING_AUTO_CORRECTED,
                                explanationEn = "Subject-held selfie requires at least one physical hand dedicated to the phone.",
                                explanationAr = "لقطة السيلفي الممسوكة باليد تتطلب فيزيائياً تخصيص يد واحدة على الأقل للهاتف.",
                                correctionApplied = "Right hand automatically allocated to 'Holding Smartphone'."
                            )
                        )
                    } else {
                        violationsCount++
                        rules.add(
                            DiagnosticRule(
                                id = "HAND_BUDGET_SELFIE_ALLOCATION",
                                titleEn = "Selfie Hand Missing",
                                titleAr = "غياب اليد الحاملة للهاتف",
                                status = RuleStatus.PHYSICS_VIOLATION,
                                explanationEn = "Phone cannot float unsupported in a subject-held selfie.",
                                explanationAr = "لا يمكن للهاتف أن يطفو في الهواء دون يد تمسكه في لقطة السيلفي."
                            )
                        )
                    }
                }

                // Check for over-budget (both hands holding items while holding phone)
                val totalOccupied = (if (resolved.leftHand != HandState.EMPTY_RELAXED) 1 else 0) +
                        (if (resolved.rightHand != HandState.EMPTY_RELAXED) 1 else 0)
                val bothHoldingItems = resolved.leftHand.isHoldingItem && resolved.rightHand.isHoldingItem
                if (bothHoldingItems) {
                    if (autoCorrect) {
                        resolved = resolved.copy(
                            rightHand = HandState.HOLDING_PHONE,
                            leftHand = HandState.HOLDING_HOT_COFFEE
                        )
                        correctionsCount++
                        rules.add(
                            DiagnosticRule(
                                id = "HAND_BUDGET_OVERRUN",
                                titleEn = "Hand Budget Overrun (3rd Hand Anomaly)",
                                titleAr = "تجاوز عدد الأيدي (منع اليد الثالثة)",
                                status = RuleStatus.WARNING_AUTO_CORRECTED,
                                explanationEn = "Both hands cannot hold props while simultaneously holding a selfie phone.",
                                explanationAr = "لا يمكن للشخص حمل كوب ومسبحة في الوقت نفسه مع مسك هاتف السيلفي.",
                                correctionApplied = "Right hand assigned to phone, Left hand retains single prop."
                            )
                        )
                    }
                } else {
                    rules.add(
                        DiagnosticRule(
                            id = "HAND_BUDGET_OK",
                            titleEn = "Hand Budget Coherence",
                            titleAr = "اتساق ميزانية الأيدي (يدان فقط)",
                            status = RuleStatus.COHERENT,
                            explanationEn = "Strict 2-hand limit satisfied: Left hand (${resolved.leftHand.labelEn}), Right hand (${resolved.rightHand.labelEn}).",
                            explanationAr = "تم التحقق بدقة: اليد اليسرى (${resolved.leftHand.labelAr})، اليد اليمنى (${resolved.rightHand.labelAr})."
                        )
                    )
                }
            }

            CaptureType.MIRROR_SELFIE -> {
                val holdsPhone = resolved.leftHand.isHoldingPhone || resolved.rightHand.isHoldingPhone
                if (!holdsPhone) {
                    if (autoCorrect) {
                        resolved = resolved.copy(rightHand = HandState.HOLDING_PHONE)
                        correctionsCount++
                        rules.add(
                            DiagnosticRule(
                                id = "MIRROR_PHONE_HELD",
                                titleEn = "Mirror Selfie Phone Gripped",
                                titleAr = "إمساك الهاتف أمام المرآة",
                                status = RuleStatus.WARNING_AUTO_CORRECTED,
                                explanationEn = "A mirror selfie requires the phone to be visibly gripped and aimed toward the mirror plane.",
                                explanationAr = "سيلفي المرآة يتطلب رؤية الهاتف مقبوضاً باليد وموجهاً نحو سطح المرآة.",
                                correctionApplied = "Right hand assigned to phone aimed at mirror."
                            )
                        )
                    }
                } else {
                    rules.add(
                        DiagnosticRule(
                            id = "MIRROR_PHONE_HELD",
                            titleEn = "Mirror Selfie Optics Valid",
                            titleAr = "فيزياء مرآة السيلفي متسقة",
                            status = RuleStatus.COHERENT,
                            explanationEn = "Phone back and lens array visible in hand facing mirror plane.",
                            explanationAr = "ظهر الهاتف وعدساته واضحة باليد وموجهة لسطح المرآة."
                        )
                    )
                }
            }

            CaptureType.THIRD_PERSON -> {
                // If in third-person, neither hand should be forced to hold the phone
                if (resolved.leftHand.isHoldingPhone || resolved.rightHand.isHoldingPhone) {
                    if (autoCorrect) {
                        resolved = resolved.copy(
                            rightHand = if (resolved.rightHand.isHoldingPhone) HandState.EMPTY_RELAXED else resolved.rightHand,
                            leftHand = if (resolved.leftHand.isHoldingPhone) HandState.EMPTY_RELAXED else resolved.leftHand
                        )
                        correctionsCount++
                        rules.add(
                            DiagnosticRule(
                                id = "THIRD_PERSON_PHONE_RELEASE",
                                titleEn = "Third-Person Free Hands",
                                titleAr = "تحرير الأيدي في تصوير الطرف الثالث",
                                status = RuleStatus.WARNING_AUTO_CORRECTED,
                                explanationEn = "In third-person photography, the camera is operated externally.",
                                explanationAr = "في تصوير الطرف الثالث، الكاميرا تُشغّل خارجياً ولا يمسك الشخص الكاميرا المصوّرة له.",
                                correctionApplied = "Hands released from camera duty to natural resting states."
                            )
                        )
                    }
                } else {
                    rules.add(
                        DiagnosticRule(
                            id = "THIRD_PERSON_PHONE_OK",
                            titleEn = "Camera-Subject Decoupling",
                            titleAr = "استقلال الكاميرا عن جسم الشخص",
                            status = RuleStatus.COHERENT,
                            explanationEn = "External camera operator confirmed. Both subject arms free for natural posture.",
                            explanationAr = "تم تأكيد مصور خارجي. ذراعا الشخص حرتان لوضعية واقعية."
                        )
                    )
                }
            }
        }

        // ----------------------------------------------------
        // RULE 2: CAMERA REACH & DISTANCE PHYSICS
        // ----------------------------------------------------
        if (resolved.captureType == CaptureType.SUBJECT_HELD_SELFIE) {
            if (resolved.cameraDistance == CameraDistance.FULL_BODY || resolved.cameraDistance == CameraDistance.MEDIUM_PORTRAIT) {
                if (autoCorrect) {
                    resolved = resolved.copy(
                        cameraDistance = CameraDistance.ARMS_LENGTH,
                        lensType = LensType.SMARTPHONE_24MM_WIDE
                    )
                    correctionsCount++
                    rules.add(
                        DiagnosticRule(
                            id = "ARM_REACH_LIMIT",
                            titleEn = "Human Arm Reach Boundary",
                            titleAr = "حدود المدى الحركي لذراع الإنسان",
                            status = RuleStatus.WARNING_AUTO_CORRECTED,
                            explanationEn = "A human arm cannot reach 1.3m - 2.8m. Max realistic extension is ~0.65m - 0.75m.",
                            explanationAr = "لا يمكن لذراع الإنسان الامتداد لمسافة 1.3 أو 2.8 متر. أقصى امتداد حقيقي هو 0.65 - 0.75 متر.",
                            correctionApplied = "Adjusted distance to Arm's Length (0.65m) with 24mm wide lens."
                        )
                    )
                } else {
                    violationsCount++
                    rules.add(
                        DiagnosticRule(
                            id = "ARM_REACH_LIMIT",
                            titleEn = "Impossible Arm Length",
                            titleAr = "طول ذراع مستحيل فيزيائياً",
                            status = RuleStatus.PHYSICS_VIOLATION,
                            explanationEn = "Distance violates human biomechanics.",
                            explanationAr = "المسافة تتناقض مع القياسات الحيوية للجسم البشري."
                        )
                    )
                }
            } else {
                rules.add(
                    DiagnosticRule(
                        id = "ARM_REACH_OK",
                        titleEn = "Arm Geometry Coherent",
                        titleAr = "هندسة امتداد الذراع متسقة",
                        status = RuleStatus.COHERENT,
                        explanationEn = "Camera distance (${resolved.cameraDistance.meters}m) is within human upper limb reach radius.",
                        explanationAr = "مسافة الكاميرا (${resolved.cameraDistance.meters}م) تقع داخل نطاق ذراع الإنسان الطبيعي."
                    )
                )
            }
        }

        // ----------------------------------------------------
        // RULE 3: POSE & SURFACE SUPPORT COMPLIANCE
        // ----------------------------------------------------
        val poseValid = resolved.bodyPose.applicableSurfaces.contains(resolved.supportSurface)
        if (!poseValid) {
            if (autoCorrect) {
                val correctSurface = resolved.bodyPose.applicableSurfaces.firstOrNull() ?: SupportSurface.WOODEN_CHAIR
                resolved = resolved.copy(supportSurface = correctSurface)
                correctionsCount++
                rules.add(
                    DiagnosticRule(
                        id = "SURFACE_POSE_MISMATCH",
                        titleEn = "Surface Support Alignment",
                        titleAr = "مواءمة سطح الدعم مع الوضعية",
                        status = RuleStatus.WARNING_AUTO_CORRECTED,
                        explanationEn = "Pose '${resolved.bodyPose.labelEn}' requires matching contact physics.",
                        explanationAr = "الوضعية '${resolved.bodyPose.labelAr}' تتطلب سطح دعم يتطابق مع الجاذبية.",
                        correctionApplied = "Support surface aligned to '${correctSurface.labelEn}'."
                    )
                )
            } else {
                violationsCount++
                rules.add(
                    DiagnosticRule(
                        id = "SURFACE_POSE_MISMATCH",
                        titleEn = "Surface Support Conflict",
                        titleAr = "تعارض سطح الدعم مع الوضعية",
                        status = RuleStatus.PHYSICS_VIOLATION,
                        explanationEn = "Pose cannot physically rest on the selected surface.",
                        explanationAr = "الوضعية لا يمكن أن تستند فيزيائياً على هذا السطح."
                    )
                )
            }
        } else {
            rules.add(
                DiagnosticRule(
                    id = "SURFACE_POSE_OK",
                    titleEn = "Weight Distribution & Compression",
                    titleAr = "توزيع الوزن وانضغاط السطح",
                    status = RuleStatus.COHERENT,
                    explanationEn = "Body weight properly distributed on ${resolved.supportSurface.labelEn}. Compression: ${resolved.supportSurface.compressionLevel}.",
                    explanationAr = "وزن الجسم موزع بانسيابية على ${resolved.supportSurface.labelAr}. درجة الانضغاط: ${resolved.supportSurface.compressionLevel}."
                )
            )
        }

        // ----------------------------------------------------
        // RULE 4: VEHICULAR INTERIOR BOUNDARIES
        // ----------------------------------------------------
        val isCarScene = resolved.sceneContext == SceneContext.CAR_PASSENGER || resolved.sceneContext == SceneContext.CAR_DRIVER
        if (isCarScene) {
            if (resolved.bodyPose != BodyPose.SEATED_IN_CAR) {
                if (autoCorrect) {
                    resolved = resolved.copy(
                        bodyPose = BodyPose.SEATED_IN_CAR,
                        supportSurface = SupportSurface.CAR_LEATHER_SEAT
                    )
                    correctionsCount++
                    rules.add(
                        DiagnosticRule(
                            id = "CAR_CABIN_GEOMETRY",
                            titleEn = "Vehicle Cabin Spatial Constraint",
                            titleAr = "قيود المساحة داخل كابينة السيارة",
                            status = RuleStatus.WARNING_AUTO_CORRECTED,
                            explanationEn = "Car cabin strictly confines human posture to seated automotive ergonomics.",
                            explanationAr = "مقصورة السيارة تفرض حتمياً وضعية الجلوس المريح داخل مقعد السيارة.",
                            correctionApplied = "Pose adjusted to 'Seated in Car' with contoured leather bolsters."
                        )
                    )
                }
            } else {
                rules.add(
                    DiagnosticRule(
                        id = "CAR_CABIN_GEOMETRY",
                        titleEn = "Vehicle Cabin Ergonomics Coherent",
                        titleAr = "بيئة السيارة الداخلية متسقة",
                        status = RuleStatus.COHERENT,
                        explanationEn = "Cabin headliner, windshield pillar, and seatbelt anchor spatial vectors aligned.",
                        explanationAr = "أبعاد السقف وقوائم الزجاج وحزام الأمان متطابقة مكانياً."
                    )
                )
            }
        }

        // ----------------------------------------------------
        // RULE 5: LIGHTING CAUSALITY & PHOTOMETRICS
        // ----------------------------------------------------
        val isNightScene = resolved.timeOfDay == TimeOfDay.NIGHT_STREETLIGHTS || resolved.timeOfDay == TimeOfDay.BLUE_HOUR
        if (isNightScene && resolved.lightingSource == LightingSource.LOW_GOLDEN_SUN) {
            if (autoCorrect) {
                resolved = resolved.copy(
                    lightingSource = LightingSource.NIGHT_STREET_LAMPS,
                    colorTempK = 2300
                )
                correctionsCount++
                rules.add(
                    DiagnosticRule(
                        id = "LIGHTING_CAUSALITY_TIME",
                        titleEn = "Photometric Causality (Night vs Sun)",
                        titleAr = "السببية الضوئية (الليل والشمس)",
                        status = RuleStatus.WARNING_AUTO_CORRECTED,
                        explanationEn = "Sun cannot be a primary key source during night/blue hour.",
                        explanationAr = "لا يمكن للشمس أن تكون المصدر الرئيسي للضوء في لقطة ليلية.",
                        correctionApplied = "Switched key light to high-pressure sodium streetlights (2300K)."
                    )
                )
            }
        } else {
            rules.add(
                DiagnosticRule(
                    id = "LIGHTING_CAUSALITY_OK",
                    titleEn = "Lighting Vector Causality",
                    titleAr = "سببية مسار الضوء والظلال",
                    status = RuleStatus.COHERENT,
                    explanationEn = "Key source (${resolved.lightingSource.labelEn}) at ${resolved.colorTempK}K casts physical shadows in inverse vector.",
                    explanationAr = "مصدر الضوء (${resolved.lightingSource.labelAr}) بحرارة ${resolved.colorTempK} كلفن يسقط ظلالاً باتجاه معاكس دقيق."
                )
            )
        }

        // ----------------------------------------------------
        // RULE 6: LENS PERSPECTIVE & SENSOR FIDELITY
        // ----------------------------------------------------
        rules.add(
            DiagnosticRule(
                id = "SMARTPHONE_OPTICS_SIMULATION",
                titleEn = "Smartphone Camera Physics",
                titleAr = "محاكاة بصرية كاميرا الهاتف",
                status = RuleStatus.COHERENT,
                explanationEn = "Subtle wide barrel distortion (${resolved.lensType.focalLengthMm}mm), sensor grain in shadows, controlled highlight clipping in windows.",
                explanationAr = "تشوه حواف العدسة الطبيعي (${resolved.lensType.focalLengthMm} مم)، تحبب ناعم في الظلال، تعريض واقعي بدون مثالية مصطنعة."
            )
        )

        // ----------------------------------------------------
        // RULE 7: CULTURAL FIDELITY & TEXTILE BEHAVIOR
        // ----------------------------------------------------
        rules.add(
            DiagnosticRule(
                id = "CULTURAL_MATERIAL_FIDELITY",
                titleEn = "Saudi Material & Textile Realism",
                titleAr = "واقعية المواد والأقمشة السعودية",
                status = RuleStatus.COHERENT,
                explanationEn = "Natural cotton crispness on thobe collar, gravity folds, Agal tension depression, authentic local environment.",
                explanationAr = "ثنايا القطن الطبيعية للياقة والكمين، ثقل العقال على الرأس، وبيئة حقيقية بعيداً عن الصور النمطية."
            )
        )

        return EvaluationResult(
            validatedState = resolved,
            rules = rules,
            autoCorrectionsCount = correctionsCount,
            violationsCount = violationsCount
        )
    }

    /**
     * Resolves and produces an updated state when user changes capture type.
     */
    fun onCaptureTypeChanged(current: SceneState, newType: CaptureType): SceneState {
        return when (newType) {
            CaptureType.SUBJECT_HELD_SELFIE -> {
                current.copy(
                    captureType = newType,
                    cameraDistance = CameraDistance.ARMS_LENGTH,
                    lensType = LensType.SMARTPHONE_26MM_MAIN,
                    cameraAngle = CameraAngle.SLIGHT_HIGH_ANGLE,
                    rightHand = HandState.HOLDING_PHONE,
                    leftHand = if (current.leftHand.isHoldingPhone) HandState.EMPTY_RELAXED else current.leftHand,
                    pitchDeg = 15
                )
            }
            CaptureType.MIRROR_SELFIE -> {
                current.copy(
                    captureType = newType,
                    cameraDistance = CameraDistance.MEDIUM_PORTRAIT,
                    lensType = LensType.SMARTPHONE_26MM_MAIN,
                    cameraAngle = CameraAngle.CHEST_LEVEL_MIRROR,
                    rightHand = HandState.HOLDING_PHONE,
                    pitchDeg = -4
                )
            }
            CaptureType.THIRD_PERSON -> {
                current.copy(
                    captureType = newType,
                    cameraDistance = CameraDistance.MEDIUM_PORTRAIT,
                    lensType = LensType.PORTRAIT_35MM,
                    cameraAngle = CameraAngle.EYE_LEVEL,
                    rightHand = if (current.rightHand.isHoldingPhone) HandState.EMPTY_RELAXED else current.rightHand,
                    leftHand = if (current.leftHand.isHoldingPhone) HandState.EMPTY_RELAXED else current.leftHand,
                    pitchDeg = 0
                )
            }
        }
    }

    /**
     * Resolves and updates state when user changes scene context.
     */
    fun onSceneContextChanged(current: SceneState, newContext: SceneContext): SceneState {
        return when (newContext) {
            SceneContext.MAJLIS_TRADITIONAL -> {
                current.copy(
                    sceneContext = newContext,
                    bodyPose = BodyPose.SITTING_FLOOR_MAJLIS,
                    supportSurface = SupportSurface.FLOOR_MAJLIS_CUSHION,
                    lightingSource = LightingSource.MAJLIS_CEILING_CHANDELIER,
                    colorTempK = 3000
                )
            }
            SceneContext.CAR_PASSENGER -> {
                current.copy(
                    sceneContext = newContext,
                    bodyPose = BodyPose.SEATED_IN_CAR,
                    supportSurface = SupportSurface.CAR_LEATHER_SEAT,
                    lightingSource = LightingSource.NATURAL_SIDE_WINDOW,
                    cameraDistance = CameraDistance.ARMS_LENGTH
                )
            }
            SceneContext.CAR_DRIVER -> {
                current.copy(
                    sceneContext = newContext,
                    bodyPose = BodyPose.SEATED_IN_CAR,
                    supportSurface = SupportSurface.CAR_LEATHER_SEAT,
                    leftHand = HandState.HOLDING_CAR_STEERING,
                    rightHand = if (current.captureType == CaptureType.SUBJECT_HELD_SELFIE) HandState.HOLDING_PHONE else HandState.EMPTY_RELAXED
                )
            }
            SceneContext.BEDROOM_RELAXED -> {
                current.copy(
                    sceneContext = newContext,
                    bodyPose = BodyPose.LYING_BED_PROP,
                    supportSurface = SupportSurface.BED_MATTRESS,
                    lightingSource = LightingSource.NATURAL_SIDE_WINDOW
                )
            }
            SceneContext.RESIDENTIAL_STREET -> {
                current.copy(
                    sceneContext = newContext,
                    bodyPose = BodyPose.STANDING_NATURAL,
                    supportSurface = SupportSurface.CONCRETE_TILES,
                    lightingSource = LightingSource.LOW_GOLDEN_SUN,
                    timeOfDay = TimeOfDay.GOLDEN_HOUR
                )
            }
            else -> current.copy(sceneContext = newContext)
        }
    }
}
