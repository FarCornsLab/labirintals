package com.labirintals.model.responses

import com.google.gson.annotations.SerializedName
import com.labirintals.model.requests.StepType
import com.labirintals.model.base.ErrorModel
import com.labirintals.server.Server

data class StepAnswer(
    @SerializedName("step_id")
    val stepId: Int? = null,
    @SerializedName("set_step_type")
    val stepType: StepType? = null,
    @SerializedName("step_end_time")
    val stepEndTime: Long? = Server.storage.lastStepTime
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}