package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.BaseModel
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.ConnectionModel
import com.labirintals.model.responses.ConnectionAnswer
import com.labirintals.server.Server
import com.labirintals.server.Server.storage
import com.labirintals.server.managers.SocketDataHolder
import java.util.*

class ConnectionCommand(args: Any?) : BaseCommand {
    companion object {
        val TAG = "connection"
    }

    private val params: ConnectionModel = Server.gson.fromJson(args.toString(), ConnectionModel::class.java)

    override fun doCommand(socketData: SocketDataHolder): String? {
        val name = params.name
        val response: ConnectionAnswer
        response = if (name != null) {
            val alreadyExist = storage.players.find { it.name == name } != null
            if (alreadyExist) {
                ConnectionAnswer(false, error = ErrorModel(ErrorCode.ErrConflict, "Имя занято"))
            } else {
                socketData.player = PlayerModel(
                    name = name,
                    cid = UUID.randomUUID().toString(),
                    oid = UUID.randomUUID().toString()
                )
                storage.players.add(socketData.player!!)
                ConnectionAnswer(true, player = socketData.player!!.toClientModel())
            }
        } else {
            ConnectionAnswer(false, error = ErrorModel(ErrorCode.ErrBadRequest, "Введите имя пользователя"))
        }
        return BaseModel(commandName = TAG, commandParams = response).toString()
    }
}