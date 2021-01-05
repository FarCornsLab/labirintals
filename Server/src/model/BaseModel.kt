package com.labirintals.model

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.ErrorModel
import com.labirintals.server.Server

data class BaseModel(
    @SerializedName("cmd")
    val commandName: String? = null,
    @SerializedName("params")
    val commandParams: Any? = null,
    @SerializedName("error")
    val error: ErrorModel? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}