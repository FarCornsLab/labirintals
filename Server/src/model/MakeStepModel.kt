package com.labirintals.model

import com.google.gson.annotations.SerializedName

data class MakeStepModel(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("step_type")
    val stepType: StepType? = null
)

enum class StepType {
    up,
    right,
    down,
    left
}