package com.labirintals.model.responses

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.ClientModel
import com.labirintals.model.base.Coords
import com.labirintals.server.Server
import com.labirintals.server.labirint.Entity

data class GetGameResultResponse(
    @SerializedName("winners")
    val winners: List<ClientModel>? = null,
    @SerializedName("step_id")
    val stepId: Int? = null,
    @SerializedName("map")
    val map: MapModel? = null,
    @SerializedName("next_step_time")
    val stepTime: Long? = Server.storage.stepTimeTo
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}

data class MapModel(
    @SerializedName("horizontal_border")
    val horizontalBorders: List<List<Entity>>? = null,
    @SerializedName("vertical_border")
    val verticalBorders: List<List<Entity>>? = null,
    @SerializedName("players_position")
    val mapPlayer: List<MapPlayer>? = null
)

data class MapPlayer(
    @SerializedName("player")
    val mPlayer: ClientModel? = null,
    @SerializedName("position")
    val mPosition: Coords? = null
)
