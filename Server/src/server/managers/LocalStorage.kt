package com.labirintals.server.managers

import com.labirintals.model.ServerSettings
import com.labirintals.model.base.PlayerModel
import com.labirintals.server.Server
import io.ktor.util.date.*
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class LocalStorage {
    private val playersSaving = "players.json"
    private val serverParamsSaving = "server.json"

    val players: ArrayList<PlayerModel> by lazy {
        ArrayList()
//        val mPlayers = readPlayers()
//        if (mPlayers == null) {
//            ArrayList()
//        } else {
//            ArrayList(mPlayers)
//        }
    }

    val serverParams: ServerSettings by lazy {
        readServerParams()
    }

    private fun getCurrentDate(): String {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    private fun readServerParams(): ServerSettings {
        val file = File(serverParamsSaving)
        if (!file.isFile) {
            file.createNewFile()
            return ServerSettings(timeStart =  getTimeMillis(), stepTime = 30)
        } else {
            return Server.gson.fromJson(file.readText(), ServerSettings::class.java)
        }
    }

    private fun readPlayers(): List<PlayerModel>? {
        val file = File(playersSaving)
        if (!file.isFile) {
            file.createNewFile()
        }

        return ArrayList() //Server.gson.fromJson(file.readText(), Array<PlayerModel>::class.java)?.toList()
    }

    private fun saveSettingParams() {
        writeFile(serverParamsSaving, serverParams.toString())
    }

    fun saveAll() {
        saveSettingParams()
        savePlayers()
    }

    private fun savePlayers() {
        writeFile(playersSaving, players.toString())
    }

    private fun writeFile(fileName: String, fileContent: String) =
        File(fileName).printWriter().use { out -> out.println(fileContent) }

    private fun clearAll(fileName: String) = File(fileName).delete()
}