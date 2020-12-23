package com.labirintals.model

import com.google.gson.annotations.SerializedName

data class BaseModel(
    @SerializedName("cmd")
    val commandName: String? = null,
    @SerializedName("params")
    val commandParams: Any? = null
)