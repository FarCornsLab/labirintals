package com.labirintals.model

import com.google.gson.annotations.SerializedName
import com.labirintals.server.Server
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ServerSettings(
    @SerializedName("time_start")
    var timeStart: Long? = null,
    @SerializedName("step_time")
    val stepTime: Long? = null  //секунды
) {
    //fun timeToString() = timeStart.toString()//timeStart?.format(DateTimeFormatter.ISO_DATE_TIME)


    override fun toString(): String {
        return Server.gson.toJson(this)
    }
}