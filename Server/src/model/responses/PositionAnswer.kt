package com.labirintals.model.responses

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.ErrorModel
import com.labirintals.server.Server

data class PositionAnswer(
    @SerializedName("step_id")
    val stepId: Int? = null,
    @SerializedName("up_border")
    val upBorder: Boolean? = null,
    @SerializedName("right_border")
    val rightBorder: Boolean? = null,
    @SerializedName("down_border")
    val down_border: Boolean? = null,
    @SerializedName("left_border")
    val left_border: Boolean? = null,
    @SerializedName("error")
    val error: ErrorModel? = null
){
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}