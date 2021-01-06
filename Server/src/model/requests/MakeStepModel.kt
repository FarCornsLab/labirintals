package com.labirintals.model.requests

import com.google.gson.annotations.SerializedName

data class MakeStepModel(
    @SerializedName("step_id")
    val stepId: Int? = 0,
    @SerializedName("step_type")
    val stepType: StepType? = null
)

enum class StepType(val index: Int) {
    up(0),
    right(1),
    down(2),
    left(3)
}