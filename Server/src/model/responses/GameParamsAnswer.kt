package com.labirintals.model.responses

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.ClientModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.server.Server

data class GameParamsAnswer(
    @SerializedName("start_time")
    val startTime: String? = "0",
    @SerializedName("step_time")
    val stepTime: Long? = null,
    @SerializedName("players")
    val players: List<ClientModel>? = null
){
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}