package com.labirintals.client

import com.labirintals.Utils.DefaultPort
import com.labirintals.Utils.selectorManager
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.util.*

object Client {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val socket = aSocket(selectorManager).tcp().connect("127.0.0.1", port = DefaultPort)
            val read = socket.openReadChannel()
            val write = socket.openWriteChannel(autoFlush = true)

            launch(Dispatchers.IO) {
                while (true) {
                    readLine(read)
//                        val line = read.readUTF8Line()
//                        if (line != null) {
//                            println("server: $line")
//                        }
                }
            }

//            if(read.availableForRead != 0){
//                val temp = ByteArray(read.availableForRead)
//                read.readAvailable(temp)
//                val text = String(temp)
//                println("server: ${text}")
//            }

            for (line in System.`in`.lines()) {
                println("client: $line")
                write.writeStringUtf8("$line\n")
            }
        }
    }

    private suspend fun readLine(read: ByteReadChannel) {
        if (read.availableForRead != 0) {
            val temp = ByteArray(read.availableForRead)
            read.readAvailable(temp)
            val text = String(temp)
            println("server: ${text}")
        }
    }

    private fun InputStream.lines() = Scanner(this).lines()

    private fun Scanner.lines() = sequence {
        while (hasNext()) {
            yield(readLine())
        }
    }
}