package com.labirintals.model.base

import com.google.gson.annotations.SerializedName
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.PositionModel
import com.labirintals.server.Server

data class PlayerModel(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("id")
    val stepId: Int? = 0,
    @SerializedName("step_type")
    val stepType: StepType? = null,
    @SerializedName("position")
    val position: PositionModel? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}

data class AllPlayersModel(
    @SerializedName("")
    val players: List<PlayerModel>? = null
)