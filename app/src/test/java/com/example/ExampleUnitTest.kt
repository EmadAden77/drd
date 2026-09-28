package com.example

import com.example.data.model.*
import com.example.engine.ConstraintEngine
import com.example.engine.PromptCompiler
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testHandBudgetEnforcement_SelfieRequiresPhoneHand() {
        // Given a selfie where neither hand holds phone
        val invalidSelfieState = SceneState(
            captureType = CaptureType.SUBJECT_HELD_SELFIE,
            leftHand = HandState.EMPTY_RELAXED,
            rightHand = HandState.EMPTY_RELAXED
        )

        val eval = ConstraintEngine.evaluate(invalidSelfieState, autoCorrect = true)

        // Engine must auto-allocate a hand to the phone
        assertTrue(eval.validatedState.leftHand.isHoldingPhone || eval.validatedState.rightHand.isHoldingPhone)
        assertTrue(eval.autoCorrectionsCount > 0)
    }

    @Test
    fun testHandBudgetOverrun_PreventThirdHand() {
        // Given both hands holding items in a selfie
        val greedyState = SceneState(
            captureType = CaptureType.SUBJECT_HELD_SELFIE,
            leftHand = HandState.HOLDING_HOT_COFFEE,
            rightHand = HandState.HOLDING_TASBEEH
        )

        val eval = ConstraintEngine.evaluate(greedyState, autoCorrect = true)

        // Cannot have both hands on items while taking a selfie
        val totalOccupied = (if (eval.validatedState.leftHand.isHoldingPhone) 1 else 0) +
                (if (eval.validatedState.rightHand.isHoldingPhone) 1 else 0)
        assertEquals(1, totalOccupied)
        assertTrue(eval.autoCorrectionsCount > 0)
    }

    @Test
    fun testCameraReachBoundary_SelfieDistanceClamped() {
        // Given an impossible distance for human arm (Full body 2.8m)
        val impossibleReachState = SceneState(
            captureType = CaptureType.SUBJECT_HELD_SELFIE,
            cameraDistance = CameraDistance.FULL_BODY
        )

        val eval = ConstraintEngine.evaluate(impossibleReachState, autoCorrect = true)

        // Distance must be clamped to human arm length
        assertEquals(CameraDistance.ARMS_LENGTH, eval.validatedState.cameraDistance)
    }

    @Test
    fun testSurfacePoseAlignment() {
        // Floor Majlis sitting cannot be on leather car seat
        val mismatchState = SceneState(
            bodyPose = BodyPose.SITTING_FLOOR_MAJLIS,
            supportSurface = SupportSurface.CAR_LEATHER_SEAT
        )

        val eval = ConstraintEngine.evaluate(mismatchState, autoCorrect = true)

        // Surface must be auto-corrected to floor cushion
        assertEquals(SupportSurface.FLOOR_MAJLIS_CUSHION, eval.validatedState.supportSurface)
    }

    @Test
    fun testPromptCompiler_HasAllTenSections() {
        val state = SceneState()
        val compiled = PromptCompiler.compile(state)

        assertEquals(10, compiled.sections.size)
        assertTrue(compiled.fullPrompt.isNotEmpty())
        assertTrue(compiled.negativePrompt.contains("third hand"))
    }

    @Test
    fun testDeterministicSeed_ExactReproduction() {
        val state1 = SceneState(seed = 999888L)
        val state2 = SceneState(seed = 999888L)

        val compiled1 = PromptCompiler.compile(state1)
        val compiled2 = PromptCompiler.compile(state2)

        assertEquals(compiled1.fullPrompt, compiled2.fullPrompt)
    }
}
