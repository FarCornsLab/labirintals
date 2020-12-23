package com.labirintals.server

import com.labirintals.model.BaseModel
import com.labirintals.model.base.Command
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.responses.ConnectionAnswer
import io.ktor.utils.io.*

class ServerManager(
    private val reader: ByteReadChannel,
    private val writer: ByteWriteChannel,
    private val storage: LocalStorage
) {

    suspend fun receiveMessage(command: String) {
        when {

            command.startsWith("/json") -> readJson(command.removePrefix("/json").trim())

            else -> writer.writeStringUtf8("$command\n")
        }
    }

    private suspend fun readJson(cmd: String) {
        try {
            val data = Server.gson.fromJson(cmd, BaseModel::class.java)
            val command = Command.getCommand(data.commandName!!, data.commandParams)
            val response = doCommand(command)
//            if (response != null) {
//                println(response.toString())
//                writer.writeStringUtf8("${response}\n")
//            } else {
//                writer.writeStringUtf8("You can try to write JSON format { name: \"name\"}\n")
//            }
        } catch (e: Exception) {
            writer.writeStringUtf8("You can try to write JSON format { name: \"name\"}\n")
        }
    }

    private suspend fun doCommand(cmd: Command?): Any? {
        when (cmd) {
            is Command.getConnection -> {
                val name = cmd.params?.userName
                val alreadyExist = storage.players.find { it.name == name } != null
                val response: ConnectionAnswer
                if (alreadyExist) {
                    response = ConnectionAnswer(false, ErrorModel(405, "Имя занято"))
                } else {
                    storage.savePlayer(PlayerModel(name = name))
                    response = ConnectionAnswer(true)
                }

                writer.writeStringUtf8("${response}\n")
            }

            is Command.getStepInfo -> {

            }

            is Command.getPosition -> {

            }

            is Command.getMakeStep -> {

            }

            is Command.getGameParams -> {

            }
        }
        return null
    }
}