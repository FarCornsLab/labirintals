package com.labirintals.server

import com.google.gson.Gson
import com.labirintals.Utils.DefaultPort
import com.labirintals.Utils.selectorManager
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.google.gson.annotations.*

data class Test(
        @SerializedName("name")
        val name: String? = null,

        @SerializedName("age")
        val age: Int? = null,

        val dotcom: String? = "www.tele.com"
) {
//    override fun toString(): String {
//        return "JSON: ${super.toString()}"
//    }
}

object Server {
    val gson = Gson()

    @JvmStatic
    fun main(args: Array<String>) {
        val port: Int
        if (args.isNotEmpty()) {
            port = args[0].toInt()
        } else {
            port = DefaultPort
        }

        runBlocking {
            val serverSocket = aSocket(selectorManager).tcp().bind(port = port)
            println("Echo Server listening at ${serverSocket.localAddress}")
            while (true) {
                val socket = serverSocket.accept()
                println("Accepted $socket")
                launch {
                    val reader = socket.openReadChannel()
                    val writer = socket.openWriteChannel(autoFlush = true)
                    val storage = LocalStorage()
                    val serverManager = ServerManager(reader, writer, storage)
                    try {
                        writer.writeStringUtf8("Successful connection!")
                        writer.writeStringUtf8(
                                "You can use commands /json { @JSONObject } or " +
                                        "write plain text"
                        )
                        while (true) {
                            val line = reader.readUTF8Line()
                            line?.let {
                                serverManager.receiveMessage(line)
                            }
                        }
                    } catch (e: Throwable) {
                        socket.close()
                    }
                }
            }
        }
    }

//    suspend fun receiveMessage(writer: ByteWriteChannel, command: String) {
//        when {
//            command.startsWith("/json") -> readJson(writer, command.removePrefix("/json").trim())
//            else -> writer.writeStringUtf8(command)
//        }
//    }
//
//
//    private suspend fun readJson(writer: ByteWriteChannel, command: String) {
//        try {
//            val data = Server.gson.fromJson(command, Test::class.java)
//            println(data.toString())
//            writer.writeStringUtf8(data.toString())
//        } catch (e: Exception) {
//            writer.writeStringUtf8("You can try to write JSON format { name: \"name\"}\n")
//        }
//    }
}