package com.labirintals.model.responses

import com.google.gson.annotations.SerializedName
import com.labirintals.model.base.ErrorModel
import com.labirintals.server.Server

class ConnectionAnswer(
    @SerializedName("is_successfully")
    val isSuccessfully: Boolean? = null,
    @SerializedName("error")
    val error: ErrorModel? = null
){
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}
