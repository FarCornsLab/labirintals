package com.labirintals.model.base

import com.google.gson.annotations.SerializedName
import com.labirintals.model.requests.StepType
import com.labirintals.model.responses.PositionModel
import com.labirintals.server.Server

data class PlayerModel(
    val name: String? = null,
    val cid: String? = null,
    val oid: String? = null,
    val stepId: Int? = 0,
    val stepType: StepType? = null,
    val position: PositionModel? = null
) {
    fun toClientModel() = ClientModel(name, oid)
}

data class ClientModel(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("oid")
    val oid: String? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}