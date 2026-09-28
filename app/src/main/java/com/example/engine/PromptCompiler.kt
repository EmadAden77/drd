package com.example.engine

import com.example.data.model.*

/**
 * Compiled prompt structure containing both modular breakdown and combined executable text.
 */
data class CompiledPrompt(
    val title: String,
    val targetModel: TargetModel,
    val seed: Long,
    val fullPrompt: String,
    val negativePrompt: String,
    val sections: List<PromptSection>
)

data class PromptSection(
    val titleEn: String,
    val titleAr: String,
    val content: String
)

/**
 * Compiles a structured, physically validated SceneState into a high-precision prompt
 * targeting photorealistic models (ChatGPT Images, Midjourney, FLUX, Gemini Imagen).
 */
object PromptCompiler {

    fun compile(state: SceneState): CompiledPrompt {
        // Pre-validate through Constraint Engine
        val evalResult = ConstraintEngine.evaluate(state, autoCorrect = true)
        val s = evalResult.validatedState

        val sections = mutableListOf<PromptSection>()

        // 1. Objective & Photographic Context
        val objectiveText = buildString {
            append("An authentic, candid smartphone photograph taken in real life. ")
            append("No studio staging, no 3D-render polish, no stock-photo glaze, no airbrushed skin. ")
            append("The image captures a genuine everyday moment in ${s.sceneContext.labelEn}. ")
            if (s.culturalFidelity == CulturalFidelity.SAUDI_AUTHENTIC) {
                append("Grounded in authentic contemporary Saudi cultural nuances and architecture. ")
            }
        }
        sections.add(
            PromptSection(
                titleEn = "1. Objective & Photographic Context",
                titleAr = "الهدف والسياق الفوتوغرافي",
                content = objectiveText
            )
        )

        // 2. Capture Architecture & Camera Geometry
        val captureGeoText = buildString {
            when (s.captureType) {
                CaptureType.SUBJECT_HELD_SELFIE -> {
                    append("Capture Style: Subject-held front-facing smartphone selfie. ")
                    append("The camera is held at natural arm's length (~${s.cameraDistance.meters}m) by the subject's ${if (s.rightHand.isHoldingPhone) "right" else "left"} arm. ")
                    append("Camera Angle: Pitch ${s.pitchDeg}°, Yaw ${s.yawDeg}°, Roll ${s.rollDeg}°. Slight high-angle perspective characteristic of a handheld mobile device. ")
                    append("The holding arm's shoulder and bicep flex naturally to support the phone's weight at the edge of the frame. ")
                }
                CaptureType.MIRROR_SELFIE -> {
                    append("Capture Style: Authentic mirror selfie in a physical glass reflection. ")
                    append("The subject holds their modern smartphone at chest/waist level, angled toward the mirror. ")
                    append("The back of the phone and its multi-lens camera array are visible in hand. ")
                    append("The reflection accurately preserves spatial depth, specular glass bevel highlights, and ambient room reflection. ")
                }
                CaptureType.THIRD_PERSON -> {
                    append("Capture Style: Third-person candid candid photograph. ")
                    append("Captured by a standing companion at eye-level (${s.cameraDistance.meters}m distance). ")
                    append("Natural candid framing with the subject relaxed and unposed. ")
                }
            }
            append("Optics: ${s.lensType.focalLengthMm}mm equivalent smartphone lens. Natural wide-angle perspective with subtle barrel geometry at the extreme borders.")
        }
        sections.add(
            PromptSection(
                titleEn = "2. Capture Architecture & Camera Geometry",
                titleAr = "هندسة الالتقاط والكاميرا",
                content = captureGeoText
            )
        )

        // 3. Subject Identity & Anatomy
        val identityText = buildString {
            append("Subject: ${s.ageGroup.labelEn} ${s.gender.labelEn.lowercase()}. ")
            append("Identity anchor: ${s.identityAnchor}. ")
            append("Facial expression: ${s.expression.labelEn}. ")
            append("Micro-expression: Completely natural, no exaggerated influencer grin, relaxed brow, genuine eye focus. ")
            when (s.hairStyle) {
                HairStyle.COVERED_SHEMAGH_AGAL -> {
                    append("Hair & Headdress: Wearing a crisp traditional Saudi Shemagh/Ghutra secured with a double black Agal. The fabric falls naturally over the shoulders with authentic fold creases; the weight of the Agal creates a gentle, realistic indent on the crown.")
                }
                HairStyle.COVERED_ABAYA_TARHA -> {
                    append("Headdress: Elegant, lightweight Tarha/headscarf draped gracefully around the face and shoulders, revealing subtle flyaway hair wisps at the temple.")
                }
                HairStyle.MODERN_TAPERED_SHORT -> {
                    append("Hair: Modern tapered clean haircut with realistic density, natural hair parting line, distinct hairline with soft baby hairs, and individual strand texture.")
                }
                HairStyle.TEXTURED_CURLS -> {
                    append("Hair: Naturally textured curls with organic volume, matte hair finish, and slight flyaway strands responding to air movement.")
                }
                HairStyle.CLASSIC_SIDE_PART -> {
                    append("Hair: Classic neat side-parted hair with soft pomade sheen and organic strand separation.")
                }
                HairStyle.SLICKED_BACK_MATTE -> {
                    append("Hair: Combed-back styling with natural scalp root visibility and matte clay texture.")
                }
            }
        }
        sections.add(
            PromptSection(
                titleEn = "3. Subject Identity, Anatomy & Hair Mechanics",
                titleAr = "الهوية وتشريح الرأس والشعر",
                content = identityText
            )
        )

        // 4. Pose Mechanics & Physical Support Points
        val poseText = buildString {
            append("Pose: ${s.bodyPose.labelEn}. ")
            append("Physical Support: Resting upon ${s.supportSurface.labelEn}. ")
            append("Biomechanics: Body weight of ~75kg exerts genuine physical downward pressure on the contact surface. ")
            when (s.supportSurface) {
                SupportSurface.FLOOR_MAJLIS_CUSHION -> {
                    append("The traditional fabric cushion visibly depresses beneath the seated thighs and ankles, creating realistic horizontal compression folds in the woven Sadu/velvet upholstery. ")
                }
                SupportSurface.CAR_LEATHER_SEAT -> {
                    append("The perforated automotive leather bolsters gently hug the torso, with natural creasing along the outer lumbar seam. ")
                }
                SupportSurface.BED_MATTRESS -> {
                    append("The soft mattress beneath the hips and elbows yields with organic concave depressions and radiating linen wrinkles. ")
                }
                SupportSurface.WOODEN_CHAIR, SupportSurface.OFFICE_CHAIR -> {
                    append("Rigid physical support with natural relaxed spinal posture, shoulders slightly slouched, and weight resting firmly on the pelvic ischial bones. ")
                }
                SupportSurface.CONCRETE_TILES, SupportSurface.MARBLE_FLOOR, SupportSurface.HARDWOOD_FLOOR -> {
                    append("Grounded foot placement with flat sole contact against the floor pavers, natural gravitational balance line through the hips. ")
                }
                SupportSurface.LEATHER_ARMCHAIR -> {
                    append("Plush cushion compression around the lower body with realistic leather grain tension. ")
                }
            }
        }
        sections.add(
            PromptSection(
                titleEn = "4. Pose & Structural Support Physics",
                titleAr = "ميكانيكا الوضعية وتوزيع الوزن",
                content = poseText
            )
        )

        // 5. Hand Budget & Interaction Matrix
        val handBudgetText = buildString {
            append("Hand Allocation Matrix (Strict 2-Hand Budget): ")
            append("Left Hand: ${s.leftHand.labelEn}. ")
            append("Right Hand: ${s.rightHand.labelEn}. ")
            append("Anatomical Check: Exactly two human hands are present in the entire scene. ")
            if (s.captureType == CaptureType.SUBJECT_HELD_SELFIE) {
                val holdingHand = if (s.rightHand.isHoldingPhone) "right" else "left"
                val freeHand = if (s.rightHand.isHoldingPhone) "left" else "right"
                append("The $holdingHand hand firmly grips the sides of the smartphone (fingers wrapping naturally around the device chassis without clipping). ")
                append("The $freeHand hand is independently engaged: ${if (s.rightHand.isHoldingPhone) s.leftHand.labelEn else s.rightHand.labelEn}. ")
            }
            append("Zero floating props, zero third-arm hallucinations, and no disembodied fingers.")
        }
        sections.add(
            PromptSection(
                titleEn = "5. Hand Budget & Object Interactions",
                titleAr = "ميزانية الأيدي والعناصر الممسوكة",
                content = handBudgetText
            )
        )

        // 6. Lighting Causality & Photometrics
        val lightingText = buildString {
            append("Illumination Source: ${s.lightingSource.labelEn} at ${s.colorTempK}K color temperature. ")
            append("Vector & Direction: ${s.lightDirection.labelEn}. ")
            append("Physical Causality: The direction of cast shadows directly opposes the primary light origin. ")
            append("Shadow hardness is ${s.shadowHardnessPercent}% with a natural penumbra gradient. ")
            append("Realistic specular catchlights appear in the cornea of both eyes matching the light source direction. ")
            append("Subsurface scattering (SSS) is subtly visible through the ear helix and nostril rim where backlighting penetrates soft tissue. ")
        }
        sections.add(
            PromptSection(
                titleEn = "6. Lighting Causality & Photometrics",
                titleAr = "فيزياء ومسار الإضاءة والظلال",
                content = lightingText
            )
        )

        // 7. Material Realism & Textiles
        val materialText = buildString {
            append("Attire: ${s.clothingType.labelEn}. ")
            when (s.clothingType) {
                ClothingType.WHITE_COTTON_THOBE -> {
                    append("Crisp Saudi white cotton fabric with starched collar structure, subtle seam stitching, and organic micro-wrinkles around the elbow joints and seated lap. Faint semi-translucent light interaction without looking synthetic. ")
                }
                ClothingType.WINTER_DARK_THOBE -> {
                    append("Heavy winter woolen/crepe blend fabric in deep charcoal or dark navy, with dense matte weave, clean tailored silhouette, and gentle drape folds. ")
                }
                ClothingType.CASUAL_POLO_JEANS -> {
                    append("Pique cotton knit texture on polo shirt with ribbed collar, paired with dark denim featuring authentic cross-hatch yarn weave and micro-creasing. ")
                }
                ClothingType.STREETWEAR_HOODIE -> {
                    append("Heavyweight french terry cotton hoodie with dropped shoulders, thick ribbed cuffs, and natural fabric bunching at the forearms. ")
                }
                ClothingType.CONTEMPORARY_ABAYA -> {
                    append("High-quality flowy crepe or lightweight linen abaya with natural matte finish, subtle tonal sleeve trim, and graceful vertical drape folds. ")
                }
                ClothingType.FORMAL_BLAZER -> {
                    append("Structured wool-blend blazer with canvassed chest lapel, authentic seam lining, and matte horn buttons. ")
                }
            }
            append("Skin Texture: Ultra-realistic human epidermis with pores, natural skin oils along the forehead and nose ridge, microscopic peach fuzz, and realistic lip creases. No digital plastic smoothing.")
        }
        sections.add(
            PromptSection(
                titleEn = "7. Materials, Fabrics & Skin Biology",
                titleAr = "واقعية الأقمشة والبشرة والمواد",
                content = materialText
            )
        )

        // 8. Smartphone Optical Imperfections
        val opticsText = buildString {
            append("Smartphone Camera Characteristics: ")
            if (s.enableSensorNoise) append("Subtle luminance sensor noise in dark shadows and low-light ambient zones. ")
            if (s.enableEdgeSoftness) append("Gentle edge optical softness and slight vignetting towards image corners. ")
            if (s.enableSubtleBarrelDistortion) append("Realistic mobile wide-angle barrel distortion (${s.lensType.focalLengthMm}mm lens characteristics). ")
            if (s.enableWindowHighlightBlowout) append("Imperfect dynamic range with mild highlight blowout in bright sunny windows or light bulbs. ")
            if (s.enableModerateHDR) append("Modern mobile computational HDR tone-mapping with realistic contrast falloff, avoiding the flat hyper-saturated AI look. ")
            append("JPEG compression artifacts consistent with high-end smartphone capture (iPhone / Google Pixel).")
        }
        sections.add(
            PromptSection(
                titleEn = "8. Smartphone Optical Imperfections",
                titleAr = "عيوب وتفاصيل كاميرا الهاتف الحقيقية",
                content = opticsText
            )
        )

        // 9. Environment & Cultural Anchors
        val envText = buildString {
            append("Environment: ${s.sceneContext.labelEn}. ")
            append("Time of Day: ${s.timeOfDay.labelEn}. ")
            when (s.sceneContext) {
                SceneContext.CAFE_CONTEMPORARY -> {
                    append("Warm modern Riyadh coffee shop interior with terrazzo/wood counter, warm exposed bulbs, small ceramic espresso cup, and subtle out-of-focus background chatter. ")
                }
                SceneContext.MAJLIS_TRADITIONAL -> {
                    append("Authentic Saudi majlis with richly patterned red/gold Sadu floor seating, traditional dallah (coffee pot) on brass tray, small finjan cups, and clean carpet flooring. ")
                }
                SceneContext.CAR_PASSENGER, SceneContext.CAR_DRIVER -> {
                    append("Contemporary car interior with tinted window glass filtering exterior sunlight, rearview mirror in corner, A/C vent details, and blurred street view outside. ")
                }
                SceneContext.RESIDENTIAL_STREET -> {
                    append("Authentic Saudi residential street in late afternoon with fabric shade canopies over parked cars, stone tile walkways, light dust patina on roadside curbs, and clear warm sky. ")
                }
                SceneContext.MODERN_OFFICE -> {
                    append("Sleek Riyadh corporate office with acoustic wall panels, ergonomic desk, laptop screen reflection, and warm recessed architectural ceiling spotlights. ")
                }
                SceneContext.BEDROOM_RELAXED -> {
                    append("Casual minimalist bedroom with soft cotton duvet, bedside wooden table with phone charging cable, and warm gentle daylight filtering through linen curtains. ")
                }
                SceneContext.DESERT_HIGHWAY_REST -> {
                    append("Highway rest station stop with desert horizon in distance, warm late-day wind, weathered asphalt, and authentic road trip ambiance. ")
                }
            }
        }
        sections.add(
            PromptSection(
                titleEn = "9. Environment & Cultural Grounding",
                titleAr = "البيئة والتفاصيل المكانية والثقافية",
                content = envText
            )
        )

        // 10. Negative Constraints & Anti-Hallucination
        val negativeText = buildString {
            append("Strict Negative Constraints: ")
            append("No third hand, no extra limbs, no floating phones, no disembodied fingers, ")
            append("no plastic waxy skin, no airbrushing, no CGI render look, no 3D octane gloss, ")
            append("no anime or illustration styles, no impossible anatomy, no warped mirror reflections, ")
            append("no fake orientalist fantasy backdrops, no disconnected light sources, ")
            append("no symmetrical mannequin eyes, no unnatural porcelain teeth.")
        }
        sections.add(
            PromptSection(
                titleEn = "10. Negative Physical Constraints",
                titleAr = "القيود السلبية ومنع التشوهات",
                content = negativeText
            )
        )

        // Generate full unified prompt based on selected target engine
        val fullPrompt = when (s.targetModel) {
            TargetModel.CHATGPT_DALL_E_3 -> {
                buildString {
                    append("A photorealistic, unposed smartphone photograph taken in real life. ")
                    append(objectiveText).append(" ")
                    append(captureGeoText).append(" ")
                    append(identityText).append(" ")
                    append(poseText).append(" ")
                    append(handBudgetText).append(" ")
                    append(lightingText).append(" ")
                    append(materialText).append(" ")
                    append(opticsText).append(" ")
                    append(envText).append(" ")
                    append("Photographic Realism Directives: ").append(negativeText)
                    append(" [Deterministic Seed: ${s.seed}]")
                }
            }
            TargetModel.MIDJOURNEY_V6 -> {
                buildString {
                    append("Raw candid smartphone photograph, ")
                    append("${s.sceneContext.labelEn}, ")
                    append("${s.captureType.labelEn}, ")
                    append("${s.lensType.focalLengthMm}mm lens, ")
                    append("${s.ageGroup.labelEn} ${s.gender.labelEn.lowercase()}, ${s.identityAnchor}, ")
                    append("${s.clothingType.labelEn}, ")
                    append("${s.lightingSource.labelEn}, ${s.colorTempK}K, natural skin texture, sensor noise, ")
                    append("subtle wide angle distortion, everyday authentic Saudi setting, uncurated real moment ")
                    append("--ar 9:16 --v 6.1 --style raw --no 3d render, plastic skin, CGI, extra hands, floating objects, airbrushing --seed ${s.seed}")
                }
            }
            TargetModel.FLUX_1 -> {
                buildString {
                    append("photo of a real person, ${s.ageGroup.labelEn} ${s.gender.labelEn.lowercase()} in ${s.sceneContext.labelEn}. ")
                    append("Shot on mobile phone camera (${s.lensType.focalLengthMm}mm), ${s.captureType.labelEn}. ")
                    append("${s.identityAnchor}. Wearing ${s.clothingType.labelEn}. ")
                    append("Pose: ${s.bodyPose.labelEn} on ${s.supportSurface.labelEn}. ")
                    append("Hands: Left hand ${s.leftHand.labelEn}, right hand ${s.rightHand.labelEn}. ")
                    append("Lighting: ${s.lightingSource.labelEn} with cast shadows at ${s.colorTempK}K. ")
                    append("Authentic camera flaws, small sensor dynamic range, natural skin pores, realistic hair wisps, no smoothing. seed:${s.seed}")
                }
            }
            TargetModel.GEMINI_IMAGEN_3 -> {
                buildString {
                    append("Create a completely realistic, high-fidelity photograph that resembles an authentic mobile phone photo. ")
                    append("Setting: ${s.sceneContext.labelEn} during ${s.timeOfDay.labelEn}. ")
                    append("Composition & Angle: ${s.captureType.labelEn}, ${s.cameraAngle.labelEn}, ${s.lensType.focalLengthMm}mm smartphone lens. ")
                    append("Subject: ${s.ageGroup.labelEn} ${s.gender.labelEn.lowercase()}, ${s.identityAnchor}, dressed in ${s.clothingType.labelEn}. ")
                    append("Physical Interaction: ${s.bodyPose.labelEn} supported by ${s.supportSurface.labelEn}. ")
                    append("Strict hand anatomy: Left hand ${s.leftHand.labelEn}, right hand ${s.rightHand.labelEn}. ")
                    append("Lighting: ${s.lightingSource.labelEn}, ${s.colorTempK}K, natural shadow falloff. ")
                    append("Sensor details: Realistic smartphone lens characteristics, gentle shadow grain, natural skin pores. ")
                    append("Negative restrictions: Avoid any artificial 3D sheen, extra fingers or arms, or stylized filters.")
                }
            }
        }

        val promptTitle = "${s.captureType.labelEn} in ${s.sceneContext.labelEn}"

        return CompiledPrompt(
            title = promptTitle,
            targetModel = s.targetModel,
            seed = s.seed,
            fullPrompt = fullPrompt,
            negativePrompt = negativeText,
            sections = sections
        )
    }
}
