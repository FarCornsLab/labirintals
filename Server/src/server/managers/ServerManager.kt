package com.labirintals.server.managers

import com.labirintals.model.BaseModel
import com.labirintals.server.Server

import com.labirintals.server.commands.CommandFactory
import io.ktor.utils.io.*

class ServerManager(
    private val reader: ByteReadChannel,
    private val writer: ByteWriteChannel
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
            val c = CommandFactory.create(data.commandName)
            val command = c(null)
            val response = command?.doCommand()
            if (response != null) {
                println(response)
                writer.writeStringUtf8("${response}\n")
            } else {
                writer.writeStringUtf8("You can try to write JSON format! { name: \"name\"}\n")
            }
        } catch (e: Exception) {
            writer.writeStringUtf8("You can try to write JSON format { name: \"name\"}\n")
        }
    }
}