package com.labirintals.model.requests

import com.google.gson.annotations.SerializedName

data class ConnectionModel(
    @SerializedName("name")
    val name: String? = null
)