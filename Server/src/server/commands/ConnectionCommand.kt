package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.ErrorNames
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.ConnectionModel
import com.labirintals.model.responses.ConnectionAnswer
import com.labirintals.server.Server
import com.labirintals.server.Server.storage
import com.labirintals.server.managers.SocketDataHolder
import io.ktor.utils.io.*
import java.util.*

class ConnectionCommand(args: Any?) : BaseCommand() {
    companion object {
        const val TAG = "connection_answer"
        const val TAG_NAME = "connection"
    }

    private val params: ConnectionModel = Server.gson.fromJson(args.toString(), ConnectionModel::class.java)

    override suspend fun doCommand(socketData: SocketDataHolder): String? {
        val token = params.token
        val name = params.name
        if(token != null){
            return if(token == Server.config.observerToken) {
                socketData.observerToken = token
                BaseModel(commandName = TAG).toString()
            }else{
                BaseModel(
                    commandName = TAG,
                    error = ErrorModel(ErrorCode.NotAuthorized, ErrorNames.ErrWrongToken)
                ).toString()
            }
        }
        val response: ConnectionAnswer
        response = if (name != null) {
            val alreadyExist = false //storage.players.find { it.name == name } != null
            if (alreadyExist) {
                return BaseModel(
                    commandName = TAG,
                    error = ErrorModel(ErrorCode.ErrConflict, ErrorNames.ErrBusyName)
                ).toString()
            } else {
                val pos = storage.labirint.spawn
                socketData.player = PlayerModel(
                    name = name,
                    cid = UUID.randomUUID().toString(),
                    oid = UUID.randomUUID().toString(),
                    coords = pos,
                    borders = storage.labirint.getBorders(pos!!)
                )
                storage.players.add(socketData.player!!)
                storage.count = storage.players.size
                ConnectionAnswer(true, player = socketData.player!!.toClientModel())
            }
        } else {
            return BaseModel(
                commandName = TAG,
                error = ErrorModel(ErrorCode.ErrBadRequest, ErrorNames.ErrNoUserName)
            ).toString()
        }
        //storage.labirint.printLabirint()
        return BaseModel(commandName = TAG, commandParams = response).toString()
    }
}