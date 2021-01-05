package com.labirintals.model.responses

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.ErrorModel
import com.labirintals.server.Server
import com.labirintals.server.labirint.Entity

data class PositionAnswer(
    @SerializedName("step_id")
    val stepId: Int? = null,
    @SerializedName("field_unit")
    val position: List<Entity>? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}

data class PositionModel(
    @SerializedName("up_border")
    val upBorder: Boolean? = null,
    @SerializedName("right_border")
    val rightBorder: Boolean? = null,
    @SerializedName("down_border")
    val down_border: Boolean? = null,
    @SerializedName("left_border")
    val left_border: Boolean? = null,
)