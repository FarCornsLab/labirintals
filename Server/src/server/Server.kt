package com.labirintals.server

import com.google.gson.Gson
import com.labirintals.Utils.DefaultPort
import com.labirintals.Utils.selectorManager
import com.labirintals.server.managers.BaseSocket
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.labirintals.server.managers.LocalStorage
import com.labirintals.server.managers.ServerManager
import com.labirintals.server.managers.SocketManager
import java.nio.charset.Charset

object Server {
    const val VERSION = "1.1.0"
    val gson = Gson()
    val storage = LocalStorage()
    val serverManager = ServerManager()

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
            println("Echo Server listening at ${serverSocket.localAddress}. Version: $VERSION")
            while (true) {
                val socket = serverSocket.accept()
                println("Accepted $socket")
                launch {
                    val baseSocket = BaseSocket(socket)
                    serverManager.sockets.add(baseSocket)
                    val socketManager = SocketManager(baseSocket)
                    try {
                        while (true) {
                            val line = baseSocket.reader.readUTF8Line()
                            line?.let {
                                println(it)
                                socketManager.receiveMessage(line)
                                //storage.saveAll()
                            }
                        }
                    } catch (e: Throwable) {
                        serverManager.sockets.remove(baseSocket)
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