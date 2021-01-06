package com.labirintals.server.managers

import com.labirintals.model.BaseModel

import com.labirintals.server.Server

import com.labirintals.server.commands.CommandFactory
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import java.util.*

class SocketManager(
    private val socket: BaseSocket
) {

    val socketData: SocketDataHolder = SocketDataHolder(socket = socket)

    suspend fun receiveMessage(command: String) {
        readJson(command)
//        when {
//            command.startsWith("/json") -> readJson(command.removePrefix("/json").trim())
//
//            else -> socket.writer.writeStringUtf8("$command\n")
//        }
    }

    private suspend fun readJson(cmd: String) {
        try {
            val data = Server.gson.fromJson(cmd, BaseModel::class.java)
            val command = CommandFactory.create(data.commandName)(data.commandParams)
            val response = command?.doCommand(socketData)
            if (response != null) {
                println(response)
                socket.writer.writeStringUtf8("${response}\n")
            } else {
                socket.writer.writeStringUtf8("You can try to write JSON format! { name: \"name\"}\n")
            }
        } catch (e: Exception) {
            socket.writer.writeStringUtf8("You can try to write JSON format { name: \"name\"}\n")
        }
    }
}