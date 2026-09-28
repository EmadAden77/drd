package com.example.data.model

/**
 * Capture type defines the physical camera rig and human-camera spatial relationship.
 */
enum class CaptureType(val labelEn: String, val labelAr: String, val description: String) {
    SUBJECT_HELD_SELFIE(
        "Subject-held Selfie",
        "سيلفي باليد",
        "Phone held directly by the subject's arm. Limits distance to arm extension and consumes at least one hand."
    ),
    MIRROR_SELFIE(
        "Mirror Selfie",
        "سيلفي بالمرآة",
        "Phone reflected in a physical mirror. Phone must be visible in hand aimed at reflection plane."
    ),
    THIRD_PERSON(
        "Third-person Photography",
        "تصوير طرف ثالث (شخص آخر)",
        "Captured by another person or positioned phone/camera. Both subject hands are completely free."
    )
}

/**
 * Scene environment context.
 */
enum class SceneContext(val labelEn: String, val labelAr: String, val category: String) {
    CAFE_CONTEMPORARY("Contemporary Cafe (Riyadh)", "مقهى عصري بالرياض", "Urban"),
    MAJLIS_TRADITIONAL("Traditional Majlis (Floor Cushions)", "مجلس تقليدي بفرش أرضي", "Cultural"),
    CAR_PASSENGER("Car Passenger Seat", "مقعد الراكب في السيارة", "Vehicular"),
    CAR_DRIVER("Car Driver Seat (Stationary)", "مقعد السائق (متوقفة)", "Vehicular"),
    RESIDENTIAL_STREET("Residential Street with Carports", "شارع سكني بمظلات سيارات", "Urban"),
    MODERN_OFFICE("Modern Saudi Tech Office", "مكتب عمل حديث", "Interior"),
    BEDROOM_RELAXED("Casual Bedroom / Home Lounge", "غرفة نوم هادئة / صالة منزلية", "Interior"),
    DESERT_HIGHWAY_REST("Desert Highway Rest Area", "استراحة طريق سريع صحراوي", "Outdoor")
}

/**
 * Cultural fidelity anchor.
 */
enum class CulturalFidelity(val labelEn: String, val labelAr: String) {
    SAUDI_AUTHENTIC("Authentic Saudi (Real Everyday Life)", "سعودي أصيل (واقعي يومي)"),
    GULF_CONTEMPORARY("Gulf Contemporary", "خليجي معاصر"),
    GLOBAL_URBAN("Global Cosmopolitan", "عالمي مدني")
}

/**
 * Physical time of day and natural sky condition.
 */
enum class TimeOfDay(val labelEn: String, val labelAr: String, val kelvinDefault: Int) {
    GOLDEN_HOUR("Golden Hour (Late Afternoon)", "العصر الذهبي (قبل الغروب)", 3200),
    DIRECT_MIDDAY("Harsh Midday Sun", "شمس الظهيرة الحادة", 5600),
    BLUE_HOUR("Blue Hour / Dusk", "ساعة الشفق الزرقاء", 7500),
    NIGHT_STREETLIGHTS("Night with Streetlights / Sodium", "ليل مع إنارة الشوارع والصوديوم", 2400),
    INDOOR_WARM_AMBIENT("Indoor Cozy Ambient (Evening)", "إضاءة داخلية دافئة", 2800),
    OVERCAST_DAYLIGHT("Diffused Overcast Sky", "نهار غائم بإضاءة مشتتة", 6500)
}

/**
 * Gender and age group.
 */
enum class Gender(val labelEn: String, val labelAr: String) {
    MALE("Male", "رجل"),
    FEMALE("Female", "امرأة")
}

enum class AgeGroup(val labelEn: String, val labelAr: String) {
    EARLY_20S("Early 20s (20-23)", "أوائل العشرينيات"),
    LATE_20S("Late 20s (26-29)", "أواخر العشرينيات"),
    MID_30S("Mid 30s (33-36)", "منتصف الثلاثينيات"),
    FORTIES("40s (40-48)", "في الأربعينيات"),
    FIFTIES("50s (50-58)", "في الخمسينيات")
}

/**
 * Facial expression.
 */
enum class Expression(val labelEn: String, val labelAr: String) {
    NEUTRAL_SUBTLE_CONFIDENT("Neutral & Subtle Confidence", "هادئ وطبيعي بثقة خفيفة"),
    NATURAL_WARM_SMILE("Gentle Natural Smile", "ابتسامة خفيفة وودودة"),
    THOUGHTFUL_CANDID("Candid & Distracted", "تأملي عفوي (غير منتبه للكاميرا)"),
    SLIGHT_SMIRK("Slight Playful Smirk", "نصف ابتسامة ذكية"),
    ENGAGED_LISTENING("Attentive & Engaged", "منتبه ومتفاعل مع المشهد")
}

/**
 * Hair style with specific anatomical hair mechanics.
 */
enum class HairStyle(val labelEn: String, val labelAr: String) {
    MODERN_TAPERED_SHORT("Modern Tapered Short Fade", "تدريج جانبي عصري وقصير"),
    TEXTURED_CURLS("Natural Textured Curls", "تموجات طبيعية بحجم متوسط"),
    CLASSIC_SIDE_PART("Classic Clean Side Part", "تقسيم جانبي كلاسيكي ومرتب"),
    COVERED_SHEMAGH_AGAL("Ghutra / Shemagh with Agal", "شماغ / غترة مع عقال أسود"),
    COVERED_ABAYA_TARHA("Hijab / Tarha Drape with Abaya", "طرحة أنيقة منسدلة مع عباية"),
    SLICKED_BACK_MATTE("Matte Slicked Back", "مشطوف للخلف بمظهر غير لامع")
}

/**
 * Clothing options with fabric behavior.
 */
enum class ClothingType(val labelEn: String, val labelAr: String) {
    WHITE_COTTON_THOBE("Crisp Saudi White Cotton Thobe", "ثوب سعودي أبيض قطني بياقة كويتية/قلابة"),
    WINTER_DARK_THOBE("Winter Dark Fabric Thobe (Charcoal/Navy)", "ثوب شتوي قماش داكن (رمادي/كحلي)"),
    CASUAL_POLO_JEANS("Casual Cotton Polo & Dark Jeans", "بولو قطني كاجوال مع جينز"),
    STREETWEAR_HOODIE("Oversized Streetwear Hoodie", "هودي عصري واسع"),
    CONTEMPORARY_ABAYA("Contemporary Linen / Crepe Abaya", "عباية عصرية من الكتان أو الكريب"),
    FORMAL_BLAZER("Smart Casual Blazer & Crew Neck", "بليزر مع تيشرت قطني رسمي")
}

/**
 * Physical pose and weight distribution.
 */
enum class BodyPose(val labelEn: String, val labelAr: String, val applicableSurfaces: List<SupportSurface>) {
    STANDING_NATURAL(
        "Standing with Natural Slouch & Weight on One Leg",
        "وقوف طبيعي بارتخاء طفيف ووزن على ساق واحدة",
        listOf(SupportSurface.CONCRETE_TILES, SupportSurface.MARBLE_FLOOR, SupportSurface.HARDWOOD_FLOOR)
    ),
    SITTING_CHAIR_RELAXED(
        "Sitting Relaxed on Chair with Elbow on Table",
        "جلوس مريح على كرسي مع وضع الكوع على الطاولة",
        listOf(SupportSurface.WOODEN_CHAIR, SupportSurface.LEATHER_ARMCHAIR, SupportSurface.OFFICE_CHAIR)
    ),
    SITTING_FLOOR_MAJLIS(
        "Cross-legged on Majlis Floor Cushion (Tarbi'ah)",
        "تربيعة أرضية على مساند المجلس مع ثقل ضاغط",
        listOf(SupportSurface.FLOOR_MAJLIS_CUSHION)
    ),
    SEATED_IN_CAR(
        "Seated in Car Seat with Back Hugging Bolsters",
        "جلوس داخل مقعد السيارة مع احتضان جانبي للمقعد",
        listOf(SupportSurface.CAR_LEATHER_SEAT)
    ),
    LYING_BED_PROP(
        "Propped up on Bed Pillows at 45-degree angle",
        "استلقاء مائل بزاوية 45 درجة على وسائد السرير",
        listOf(SupportSurface.BED_MATTRESS)
    ),
    WALKING_STREET(
        "Mid-stride Walking Candid on Pavement",
        "حركة مشي طبيعية في منتصف الخطوة على الرصيف",
        listOf(SupportSurface.CONCRETE_TILES, SupportSurface.MARBLE_FLOOR)
    )
}

/**
 * Physical support surface that receives weight and compresses.
 */
enum class SupportSurface(val labelEn: String, val labelAr: String, val compressionLevel: String) {
    FLOOR_MAJLIS_CUSHION("Majlis Floor Cushion & Sadu Rug", "مسند وسجادة مجلس أرضي", "High fabric indentation"),
    CAR_LEATHER_SEAT("Automotive Perforated Leather Seat", "مقعد سيارة جلد مثقب", "Firm contour hugging"),
    BED_MATTRESS("Foam & Cotton Bed Mattress", "مرتبة سرير قطنية وإسفنجية", "Deep soft compression wrinkles"),
    WOODEN_CHAIR("Wooden Cafe Chair", "كرسي مقهى خشبي", "Rigid surface, zero indentation"),
    LEATHER_ARMCHAIR("Plush Leather Armchair", "أريكة جلدية وثيرة", "Moderate cushion compression"),
    OFFICE_CHAIR("Ergonomic Mesh Office Chair", "كرسي مكتب شبكي مريح", "Tension mesh deflection"),
    CONCRETE_TILES("Sidewalk Pavers / Concrete", "بلاط رصيف خارجي خرساني", "Hard planar surface"),
    MARBLE_FLOOR("Polished Marble Flooring", "أرضية رخام مصقولة", "Rigid reflective surface"),
    HARDWOOD_FLOOR("Hardwood Interior Flooring", "أرضية خشبية داخلية", "Rigid planar surface")
}

/**
 * Camera angle, height, and orientation.
 */
enum class CameraAngle(val labelEn: String, val labelAr: String, val pitchDeg: Int) {
    SLIGHT_HIGH_ANGLE("Slight High Angle (Classic Selfie Elevation)", "زاوية علوية خفيفة (سيلفي كلاسيكي)", 15),
    EYE_LEVEL("Direct Eye Level (Neutral Perspective)", "مستوى العين المباشر (منظور محايد)", 0),
    SLIGHT_LOW_ANGLE("Slight Low Angle (Heroic / Grounded)", "زاوية سفلية خفيفة (مظهر هادئ وحاسم)", -12),
    CHEST_LEVEL_MIRROR("Chest Level Pointed at Mirror", "مستوى الصدر موجه نحو المرآة", -5),
    OVER_THE_SHOULDER("Over-the-shoulder Candid", "من خلف الكتف (لقطة عفوية)", 0)
}

/**
 * Distance between phone/camera and the subject.
 */
enum class CameraDistance(val labelEn: String, val labelAr: String, val meters: Float) {
    CLOSE_UP_FACE("Close-up / Headshot (0.4m)", "لقطة قريبة للوجه (0.4 متر)", 0.4f),
    ARMS_LENGTH("Arm's Length (0.65m)", "مدى طول الذراع (0.65 متر)", 0.65f),
    MEDIUM_PORTRAIT("Medium Shot / Waist-up (1.3m)", "لقطة متوسطة للخصر (1.3 متر)", 1.3f),
    FULL_BODY("Full Body Shot (2.8m)", "لقطة كاملة للجسم (2.8 متر)", 2.8f)
}

/**
 * Lens focal length simulation.
 */
enum class LensType(val labelEn: String, val labelAr: String, val focalLengthMm: Int) {
    SMARTPHONE_24MM_WIDE("24mm eq. Smartphone Ultrawide/Wide", "عدسة هاتف عريضة 24 مم", 24),
    SMARTPHONE_26MM_MAIN("26mm eq. Smartphone Primary Camera", "عدسة هاتف أساسية 26 مم (آيفون/بيكسل)", 26),
    PORTRAIT_35MM("35mm Documentary / Street Lens", "عدسة 35 مم تصوير وثائقي وشوارع", 35),
    CLASSIC_50MM("50mm Standard Human Eye Perspective", "عدسة 50 مم رؤية بشرية قياسية", 50),
    TELEPHOTO_85MM("85mm Portrait Compression Lens", "عدسة 85 مم بورتريه وعزل احترافي", 85)
}

/**
 * Hand states for hand budget tracking.
 */
enum class HandState(val labelEn: String, val labelAr: String, val isHoldingPhone: Boolean, val isHoldingItem: Boolean) {
    EMPTY_RELAXED("Empty & Naturally Relaxed", "فارغة ومسترخية طبيعيًا", false, false),
    HOLDING_PHONE("Holding Smartphone (Camera Rig)", "تمسك الهاتف للتصوير", true, false),
    HOLDING_HOT_COFFEE("Holding Ceramic Coffee Cup", "تمسك كوب قهوة ساخنة", false, true),
    HOLDING_TASBEEH("Fingering Olive-Wood Tasbeeh (Misbaha)", "تمسك مسبحة خشب زيتون", false, true),
    HOLDING_CAR_STEERING("Resting on Car Steering Wheel", "على مقود السيارة", false, false),
    IN_POCKET("Resting Inside Trouser / Thobe Pocket", "مستقرة داخل الجيب", false, false),
    RESTING_ON_LAP("Resting Open on Thigh / Lap", "مستريحة على الفخذ / الحجر", false, false),
    RESTING_ON_TABLE("Resting Flat on Table Surface", "مرتاحة على سطح الطاولة", false, false),
    TOUCHING_BEARD_CHIN("Gently Touching Chin / Beard", "تلمس الذقن أو اللحية برفق", false, false)
}

/**
 * Primary physical light source.
 */
enum class LightingSource(val labelEn: String, val labelAr: String, val defaultTempK: Int) {
    NATURAL_SIDE_WINDOW("Large Side Window Daylight", "نافذة جانبية كبيرة بضوء النهار", 5500),
    LOW_GOLDEN_SUN("Low-Angle Setting Sun (Direct Beam)", "شمس الغروب المائلة المباشرة", 3100),
    CAFE_PENDANT_LAMPS("Pendant Warm Filament Lamps", "مصابيح متدلية دافئة بالمقهى", 2700),
    MAJLIS_CEILING_CHANDELIER("Central Majlis Warm Chandelier", "ثريا مجلس مركزية دافئة", 3000),
    NIGHT_STREET_LAMPS("High-pressure Sodium Streetlights", "أعمدة إنارة الشارع الصفراء", 2200),
    SMARTPHONE_SCREEN_GLOW("Cold Ambient Screen Glow", "وهج شاشة الهاتف الباردة", 6200),
    CAR_DASHBOARD_AMBIENT("Car Dashboard Glow & Night Ambient", "إضاءة طبلون السيارة وأجواء الليل", 3500)
}

/**
 * Light direction relative to subject.
 */
enum class LightDirection(val labelEn: String, val labelAr: String) {
    FRONT_45_KEY("45° Front-Left Key Light (Natural Dimension)", "إضاءة رئيسية بزاوية 45 درجة أمامية يسارية"),
    HARD_SIDE_LIGHT("90° Direct Side Light (High Sculpted Shadows)", "إضاءة جانبية حادة 90 درجة"),
    BACKLIT_RIM("Backlit with Subtle Rim Glow on Shoulders/Hair", "إضاءة خلفية مع لمعان حواف الكتفين والشعر"),
    DIFFUSE_FRONTAL("Diffuse Frontal Illumination (Flattering)", "إضاءة أمامية ناعمة ومشتتة"),
    OVERHEAD_DOWN("Steep Overhead Downward Light", "إضاءة عمودية من الأعلى لأسفل")
}

/**
 * Target Image Generation Model.
 */
enum class TargetModel(val labelEn: String, val modelTag: String) {
    CHATGPT_DALL_E_3("ChatGPT Images (GPT-4o)", "gpt4o-images"),
    MIDJOURNEY_V6("Midjourney v6.1", "mj-v6"),
    FLUX_1("FLUX.1 Schnell / Dev", "flux1-dev"),
    GEMINI_IMAGEN_3("Gemini Imagen 3", "gemini-imagen3")
}

/**
 * Diagnostics rule validation outcome.
 */
enum class RuleStatus {
    COHERENT,
    WARNING_AUTO_CORRECTED,
    PHYSICS_VIOLATION
}

data class DiagnosticRule(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val status: RuleStatus,
    val explanationEn: String,
    val explanationAr: String,
    val correctionApplied: String? = null
)
