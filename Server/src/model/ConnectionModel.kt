package com.labirintals.model

import com.google.gson.annotations.SerializedName

data class ConnectionModel(
    @SerializedName("user_name")
    val userName: String? = null
)