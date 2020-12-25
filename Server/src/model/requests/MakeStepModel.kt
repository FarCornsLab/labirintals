package com.labirintals.model.requests

import com.google.gson.annotations.SerializedName

data class MakeStepModel(
    @SerializedName("step_id")
    val stepid: Int? = 0,
    @SerializedName("step_type")
    val stepType: StepType? = null
)

enum class StepType {
    up,
    right,
    down,
    left
}