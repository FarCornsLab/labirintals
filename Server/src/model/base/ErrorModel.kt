package com.labirintals.model.base

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null
)