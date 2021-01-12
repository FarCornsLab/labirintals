package com.labirintals.server.managers

import com.labirintals.model.ServerSettings
import com.labirintals.model.base.ClientModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.server.Server
import com.labirintals.server.labirint.Labirint
import io.ktor.util.date.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.properties.Delegates

class LocalStorage {

    private val playersSaving = "players.json"
    private val serverParamsSaving = "server.json"

    var gameIsStarted = false
    var stepTimeTo: Long? = null
    val players = ArrayList<PlayerModel>()
    var globalStep = 0
    var finalStep = 0
    val newPlayers = ArrayList<PlayerModel>()
    val winners = ArrayList<ClientModel>()

    private fun startTimer() {
        serverParams.timeStart = getTimeMillis() + TimeUnit.SECONDS.toMillis(Server.config.waitingTime)
        stepTimeTo = serverParams.timeStart!! + TimeUnit.SECONDS.toMillis(Server.config.stepTime)
        Timer().schedule(TimeUnit.SECONDS.toMillis(Server.config.waitingTime)) {
            gameIsStarted = true
            globalStep = 1
            startGame()
        }
    }

    private fun startGame() {
        if (gameIsStarted) {
            Timer().schedule(TimeUnit.SECONDS.toMillis(Server.config.stepTime)) {
                nextStep()
                stepTimeTo = getTimeMillis() + TimeUnit.SECONDS.toMillis(Server.config.stepTime)
            }
        }
    }

    private fun nextStep() {
        var gameIsEnd = false
        players.forEachIndexed { index, oldPlayer ->
            val player = newPlayers.find { oldPlayer.oid == it.oid && oldPlayer.cid == it.cid }
            if (player != null) {
                players[index] = player
                if (player.stepId == PlayerModel.END_GAME) {
                    winners.add(player.toClientModel())
                    gameIsEnd = true
                }
            }
        }
        if (gameIsEnd) {
            finalStep = globalStep
            globalStep = PlayerModel.END_GAME
            gameIsStarted = false
        } else {
            globalStep += 1
            startGame()
        }
    }

    var count: Int by Delegates.observable(0) { _, _, value ->
        if (value >= 2) {
            startTimer()
        }
    }

    val labirint = Labirint.read("labirint.json")

    val serverParams: ServerSettings by lazy {
        ServerSettings(timeStart = 0, stepTime = Server.config.waitingTime)
        //readServerParams()
    }

    private fun readServerParams(): ServerSettings {
        val file = File(serverParamsSaving)
        if (!file.isFile) {
            file.createNewFile()
            return ServerSettings(timeStart = 0, stepTime = 30)
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
        //savePlayers()
    }

    private fun savePlayers() {
        writeFile(playersSaving, players.toString())
    }

    private fun writeFile(fileName: String, fileContent: String) =
        File(fileName).printWriter().use { out -> out.println(fileContent) }

    private fun clearAll(fileName: String) = File(fileName).delete()
}