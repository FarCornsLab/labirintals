package com.labirintals.model.base

import com.google.gson.annotations.SerializedName
import com.labirintals.server.Server

data class PlayerModel(
    @SerializedName("name")
    val name: String? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}