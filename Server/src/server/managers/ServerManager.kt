package com.labirintals.server.managers

import io.ktor.network.sockets.*
import io.ktor.utils.io.*

class ServerManager {
    val sockets = ArrayList<BaseSocket>()

    fun setDefault(){
        sockets.clear()
    }
}

class BaseSocket(
    val mSocket: Socket
){
    val reader: ByteReadChannel = mSocket.openReadChannel()
    val writer: ByteWriteChannel = mSocket.openWriteChannel(true)
}