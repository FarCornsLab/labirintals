package com.labirintals.server

import com.labirintals.model.BaseModel
import com.labirintals.model.base.PlayerModel
import java.io.File

class LocalStorage {
    private val playersSaving = "players.txt"
    private val serverSaving = "server.txt"

    val players = arrayListOf<PlayerModel>()


    init {
        readPlayers()

    }

    private fun readPlayers() {
        val file = File(playersSaving)
        if (!file.isFile) {
            file.createNewFile()
        }
        file.forEachLine {
            val model = Server.gson.fromJson(it, PlayerModel::class.java)
            players.add(model)
        }
    }

    fun savePlayer(player: PlayerModel){
        players.add(player)
        writeFile(playersSaving, player.toString())
    }

    fun savePlayers() {
        clearAll(playersSaving)
        writeFile(playersSaving, players.toString())
    }

//    private fun readServer(){
//        File(serverSaving).useLines { it.toString() }
//    }

    //private fun<T> readFileAsLinesUsingUseLines(fileName: String): List<T> = File(fileName).useLines { it.toList() }

    private fun writeFile(fileName: String, fileContent: String) =
        File(fileName).printWriter().use { out -> out.println(fileContent) }

    private fun clearAll(fileName: String) = File(fileName).delete()
}