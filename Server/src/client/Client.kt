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
                    val line = read.readUTF8Line()
                    println("server: $line")
                }
            }
            if(read.availableForRead != 0){
                val temp = ByteArray(read.availableForRead)
                val t = read.readAvailable(temp)
                val text = String(temp)
                println("server: ${text}")
            }

            for (line in System.`in`.lines()) {
                println("client: $line")
                write.writeStringUtf8("$line\n")
            }
        }
    }

    private fun InputStream.lines() = Scanner(this).lines()

    private fun Scanner.lines() = sequence {
        while (hasNext()) {
            yield(readLine())
        }
    }
}