package com.labirintals.server

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


    private suspend fun readJson(command: String) {
        try {
            val data = Server.gson.fromJson(command, Test::class.java)
            println(data.toString())
            writer.writeStringUtf8("${data}\n")
        } catch (e: Exception) {
            writer.writeStringUtf8("You can try to write JSON format { name: \"name\"}\n")
        }
    }
}