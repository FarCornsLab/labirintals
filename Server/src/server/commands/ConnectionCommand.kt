package com.labirintals.server.commands

import com.labirintals.ErrorCode
import com.labirintals.model.base.ErrorModel
import com.labirintals.model.base.PlayerModel
import com.labirintals.model.requests.ConnectionModel
import com.labirintals.model.responses.ConnectionAnswer
import com.labirintals.server.Server
import com.labirintals.server.Server.storage
import io.ktor.utils.io.*

class ConnectionCommand(args: Any?): BaseCommand {
    private val params: ConnectionModel = Server.gson.fromJson(args.toString(), ConnectionModel::class.java)

    override fun doCommand(): String? {
        val name = params.userName
        val response: ConnectionAnswer
        response = if(name != null) {
            val alreadyExist = storage.players.find { it.name == name } != null
            if (alreadyExist) {
                ConnectionAnswer(false, ErrorModel(ErrorCode.ErrConflict, "Имя занято"))
            } else {
                storage.players.add(PlayerModel(name = name))
                ConnectionAnswer(true)
            }
        }else{
            ConnectionAnswer(false, ErrorModel(ErrorCode.ErrBadRequest, "Введите имя пользователя"))
        }
        return response.toString()
    }
}