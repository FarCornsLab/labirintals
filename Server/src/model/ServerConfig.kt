package com.labirintals.model

import com.google.gson.annotations.SerializedName
import com.labirintals.server.Server

data class ServerConfig(
    @SerializedName("ip")
    val ip: String = "127.0.0.1",
    @SerializedName("port")
    val port: Int = 9999,
    @SerializedName("WAITING_TIME")
    val waitingTime: Long = 60L,
    @SerializedName("STEP_TIME")
    val stepTime: Long = 30L,
    @SerializedName("OBSERVER_TOKEN")
    val observerToken: String? = null
) {
    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}