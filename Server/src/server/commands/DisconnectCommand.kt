package com.labirintals.server.commands

import com.labirintals.model.BaseModel
import com.labirintals.server.Server
import com.labirintals.server.managers.SocketDataHolder

class DisconnectCommand: BaseCommand() {
    companion object {
        const val TAG = "disconnect"
        const val TAG_NAME = "disconnect"
    }

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val player = Server.storage.players.find { it.cid == socketData.player?.cid && it.oid == socketData.player?.oid}
        if(player != null){
            //val index = Server.storage.players.indexOf(player)
            Server.storage.players.remove(player)
            Server.storage.newPlayers.remove(player)
            socketData.player = null
            if(Server.storage.players.isEmpty()){
                Server.reload()
            }
        }
        socketData.observerToken = null
        Server.serverManager.sockets.remove(socketData.socket)
        socketData.socket.mSocket.close()
        return BaseModel(commandName = TAG).toString()
    }
}